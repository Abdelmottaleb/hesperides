/*
 *
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
 *
 */

package com.vsct.dt.hesperides.util.converter;

import com.vsct.dt.hesperides.resources.TimeStampedPlatform;
import com.vsct.dt.hesperides.templating.platform.TimeStampedPlatformData;

/**
 * Created by emeric_martineau on 27/10/2015.
 */
public interface TimeStampedPlatformConverter {
    /**
     * Return platform converter.
     *
     * @return platform converter
     */
    PlatformConverter getPlatformConverter();

    /**
     * Convert TimeStampedPlatformData to TimeStampedPlatform.
     * @param timeStampedPlatformData input
     * @return output
     */
    TimeStampedPlatform toTimeStampedPlatform(TimeStampedPlatformData timeStampedPlatformData);

    /**
     * Convert TimeStampedPlatform to TimeStampedPlatformData.
     * @param timeStampedPlatform input
     * @return output
     */
    TimeStampedPlatformData toTimeStampedPlatformData(TimeStampedPlatform timeStampedPlatform);
}
