/*
 *
 * This file is part of the Hesperides distribution.
 * (https://github.com/voyages-sncf-technologies/hesperides)
 * Copyright (c) 2016 VSCT.
 *
 * Hesperides is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3.
 *
 * Hesperides is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

import com.vsct.dt.hesperides.api.ModuleApi;
import com.vsct.dt.hesperides.api.authentication.SimpleAuthenticator;
import com.vsct.dt.hesperides.api.authentication.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.ClassRule;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ProtectedAccessTest extends CucumberTest {
    private static final String AUTHENTICATION_TOKEN = "Sm9obl9Eb2U6c2VjcmV0";

    private static final BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER =
            new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new SimpleAuthenticator())
                    .setPrefix("Basic")
                    .setRealm("AUTHENTICATION_PROVIDER")
                    .buildAuthFilter();

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(mock(ModuleApi.class))
            .build();

    private Response response;

    private Response query(final String url, boolean isAuthenticated) {
        Invocation.Builder requestBuilder = RULE.getJerseyTest().target(url).request();
        if (isAuthenticated) {
            requestBuilder.header("Authorization", "Basic " + AUTHENTICATION_TOKEN);
        }
        return requestBuilder.get();
    }

    public ProtectedAccessTest() {
        When("^an (un)?authenticated user tries to retrieve the modules name$", (final String notAuthenticated) -> {
            boolean isAuthenticated = StringUtils.isEmpty(notAuthenticated);
            response = query("/toto", isAuthenticated);
        });

        Then("^he should( not)? be authorized to get them$", (final String notAuthorized) -> {
            Response.Status expectedStatus = StringUtils.isBlank(notAuthorized) ? Response.Status.OK : Response.Status.UNAUTHORIZED;
            assertThat(response.getStatus()).isEqualTo(expectedStatus.getStatusCode());
        });
    }
}
