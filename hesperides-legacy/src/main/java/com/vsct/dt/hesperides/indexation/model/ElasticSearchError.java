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

package com.vsct.dt.hesperides.indexation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by william on 05/09/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchError {

    private String error;
    private String status;

    public final void setError(final String error) {
        this.error = error;
    }

    public final String getError() {
        return this.error;
    }

    public final void setStatus(final String status) {
        this.status = status;
    }

    public final String getStatus() {
        return status;
    }

    @JsonIgnore
    public final boolean isError() {
        return error != null;
    }

}
