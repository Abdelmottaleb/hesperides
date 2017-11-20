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

package com.vsct.dt.hesperides.applications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by william_montaz on 17/04/2015.
 */
public class PlatformDeletedEvent {
    private final String applicationName;
    private final String platformName;

    @JsonCreator
    public PlatformDeletedEvent(@JsonProperty("applicationName") String applicationName,
                                @JsonProperty("platformName") String platformName) {
        this.applicationName = applicationName;
        this.platformName = platformName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getPlatformName() {
        return platformName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlatformDeletedEvent that = (PlatformDeletedEvent) o;

        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        if (platformName != null ? !platformName.equals(that.platformName) : that.platformName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = applicationName != null ? applicationName.hashCode() : 0;
        result = 31 * result + (platformName != null ? platformName.hashCode() : 0);
        return result;
    }


}
