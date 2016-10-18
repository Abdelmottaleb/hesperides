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

package com.vsct.dt.hesperides;

import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration;
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;
import com.vsct.dt.hesperides.events.EventsConfiguration;
import com.vsct.dt.hesperides.indexation.ElasticSearchConfiguration;
import com.vsct.dt.hesperides.security.LDAPAuthenticator;
import com.vsct.dt.hesperides.security.LdapConfiguration;
import com.vsct.dt.hesperides.security.SimpleAuthenticator;
import com.vsct.dt.hesperides.security.model.User;
import com.vsct.dt.hesperides.storage.RedisConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.client.HttpClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public final class HesperidesConfiguration extends Configuration implements AssetsBundleConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = new AssetsConfiguration();

    @Valid
    @NotNull
    @JsonProperty
    private CacheBuilderSpec authenticationCachePolicy;

    @Valid
    @NotEmpty
    private String apiVersion;

    @Valid
    @NotEmpty
    private String backendVersion;

    @Valid
    @NotNull
    @JsonProperty
    private ElasticSearchConfiguration elasticSearchConfiguration;

    @Valid
    @NotNull
    @JsonProperty
    private RedisConfiguration redisConfiguration;

    @Valid
    @NotNull
    @JsonProperty
    private LdapConfiguration ldapConfiguration;

    @Override
    public AssetsConfiguration getAssetsConfiguration() {
        return assets;
    }

    @Valid
    @NotNull
    @JsonProperty
    private final HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();

    @Valid
    @NotEmpty
    private String authenticatorType;

    @Valid
    @NotEmpty
    private String useDefaultUserWhenAuthentFails;

    @Valid
    @NotNull
    @JsonProperty
    private EventsConfiguration eventsConfiguration;

    public HesperidesConfiguration() {
    }

    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

    @JsonProperty
    public void setAuthenticatorType(final String authenticatorType) {
        this.authenticatorType = authenticatorType;
    }

    public CacheBuilderSpec getAuthenticationCachePolicy() {
        return authenticationCachePolicy;
    }

    public void setAuthenticationCachePolicy(final CacheBuilderSpec authenticationCachePolicy) {
        this.authenticationCachePolicy = authenticationCachePolicy;
    }

    @JsonIgnore
    public Optional<Authenticator<BasicCredentials, User>> getAuthenticator() {
        if (authenticatorType.equals("none")) {
            return Optional.empty();
        }
        else if (authenticatorType.equals("simple")) {
            return Optional.of(new SimpleAuthenticator());
        }
        else if (authenticatorType.equals("LDAP")) {
            return Optional.of(new LDAPAuthenticator(getLdapConfiguration()));
        }
        else {
            throw new IllegalArgumentException("Authenticator " + authenticatorType + " is unknow. Use one of ['none', 'simple', 'LDAP']");
        }
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getBackendVersion() {
        return backendVersion;
    }

    public void setBackendVersion(String backendVersion) {
        this.backendVersion = backendVersion;
    }

    public ElasticSearchConfiguration getElasticSearchConfiguration() {
        return elasticSearchConfiguration;
    }

    public void setElasticSearchConfiguration(ElasticSearchConfiguration elasticSearchConfiguration) {
        this.elasticSearchConfiguration = elasticSearchConfiguration;
    }

    public RedisConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public String getAuthenticatorType() {
        return authenticatorType;
    }

    public LdapConfiguration getLdapConfiguration() {
        return ldapConfiguration;
    }

    public void setLdapConfiguration(LdapConfiguration ldapConfiguration) {
        this.ldapConfiguration = ldapConfiguration;
    }

    public boolean useDefaultUserWhenAuthentFails() {
        return useDefaultUserWhenAuthentFails.equals("true");
    }

    public void setUseDefaultUserWhenAuthentFails(String useDefaultUserWhenAuthentFails) {
        this.useDefaultUserWhenAuthentFails = useDefaultUserWhenAuthentFails;
    }

    public void setEventsConfiguration (EventsConfiguration eventsConfiguration){
        this.eventsConfiguration = eventsConfiguration;
    }

    public EventsConfiguration getEventsConfiguration (){
        return this.eventsConfiguration;
    }
}
