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

package com.vsct.dt.hesperides.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import redis.clients.jedis.Protocol;

import java.util.Set;

/**
 * Created by william_montaz on 29/01/2015.
 */
public class RedisConfiguration {

    public enum Type {
        REDIS, SENTINEL
    }

    @JsonProperty
    private Type type;

    /* Specific redis */
    @JsonProperty
    private String host;

    @JsonProperty
    private int    port;

    /* Specific sentinel */
    @JsonProperty
    private String masterName;

    @JsonProperty
    private Set<String> sentinels;

    /* All */
    @JsonProperty
    private int retry;

    @JsonProperty
    private int waitBeforeRetryMs;

    @JsonProperty
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public Set<String> getSentinels() {
        return sentinels;
    }

    public void setSentinels(Set<String> sentinels) {
        this.sentinels = sentinels;
    }

    public Type getType() {
        return type;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getWaitBeforeRetryMs() {
        return waitBeforeRetryMs;
    }

    public void setWaitBeforeRetryMs(int waitBeforeRetryMs) {
        this.waitBeforeRetryMs = waitBeforeRetryMs;
    }

    public int getTimeout() {
        return timeout;
    }
}
