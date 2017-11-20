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

package com.vsct.dt.hesperides.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.vsct.dt.hesperides.storage.EventStore;

/**
 * Created by william_montaz on 04/02/2015.
 */
public class EventStoreHealthCheck extends HealthCheck {

    private final EventStore store;

    public EventStoreHealthCheck(EventStore store){
        this.store = store;
    }

    @Override
    protected Result check() throws Exception {
        if(store.isConnected()){
            return Result.healthy();
        } else {
            return Result.unhealthy("Cannot connect to event store.");
        }
    }
}
