/*
 *  * This file is part of the Hesperides distribution.
 *  * (https://github.com/voyages-sncf-technologies/hesperides)
 *  * Copyright (c) 2016 VSCT.
 *  *
 *  * Hesperides is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as
 *  * published by the Free Software Foundation, version 3.
 *  *
 *  * Hesperides is distributed in the hope that it will be useful, but
 *  * WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.vsct.dt.hesperides.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import com.vsct.dt.hesperides.HesperidesCacheParameter;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by emeric_martineau on 28/01/2016.
 */
public class HesperidesCacheBuilder {

    /**
     * Create cache with easy setup.
     *
     * @param config setup
     *
     * @return cache
     */
    public static CacheBuilder<Object, Object> newBuilder(final HesperidesCacheParameter config) {
        return newBuilder(config, null);
    }

    /**
     * Create cache with easy setup.
     *
     * @param config setup
     * @param weigher Guava weighter
     *
     * @return cache
     */
    public static CacheBuilder<Object, Object> newBuilder(final HesperidesCacheParameter config,
                                                          final Weigher<? extends Object, ? extends Object> weigher) {
        final CacheBuilder<Object, Object> cache = CacheBuilder.newBuilder();

        if (config != null) {
            final int maxSize = config.getMaxSize();
            final int weight = config.getWeight();
            final String expire = config.getItemExpireAfter();

            if (maxSize != HesperidesCacheParameter.NOT_SET) {
                cache.maximumSize(maxSize);
            }

            if (weight != HesperidesCacheParameter.NOT_SET) {
                if (weigher == null) {
                    throw new IllegalArgumentException("Parameter 'weight' is not supported for this cache.");
                }

                cache.maximumWeight(weight);
            }

            if (expire != null) {
                final Pattern p = Pattern.compile("^([0-9]+)(m|s|h|d)");
                final Matcher m = p.matcher(expire);

                if (m.find()) {
                    final int time = Integer.valueOf(m.group(1));
                    TimeUnit unit = TimeUnit.SECONDS;

                    switch (m.group(2)) {
                        case "m":
                            unit = TimeUnit.MINUTES;
                            break;
                        case "h":
                            unit = TimeUnit.HOURS;
                            break;
                        case "d":
                            unit = TimeUnit.DAYS;
                            break;
                        default:
                            // Nothing
                    }

                    cache.expireAfterWrite(time, unit);
                    cache.expireAfterAccess(time, unit);
                } else {
                    throw new IllegalArgumentException("Parameter 'itemExpireAfter' is not valid. Valid usage is [0-9]+(m|h|d|s). (Where 'm' is minutes, 'h' is hours, 'd' is days, 's' seconds.");
                }
            }
        }

        return cache;
    }

}
