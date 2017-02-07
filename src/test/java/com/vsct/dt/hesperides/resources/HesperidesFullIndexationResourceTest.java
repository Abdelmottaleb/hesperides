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

package com.vsct.dt.hesperides.resources;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

import com.vsct.dt.hesperides.applications.ApplicationsAggregate;
import com.vsct.dt.hesperides.indexation.ElasticSearchIndexationExecutor;
import com.vsct.dt.hesperides.templating.modules.ModulesAggregate;
import com.vsct.dt.hesperides.templating.packages.TemplatePackagesAggregate;

/**
 * Created by william_montaz on 01/09/14.
 */
/* AUTHENTICATION -> John_Doe:secret => Basic Sm9obl9Eb2U6c2VjcmV0 */
public class HesperidesFullIndexationResourceTest extends AbstractTechUserResourceTest {

    private static final ModulesAggregate MODULES_AGGREGATE = mock(ModulesAggregate.class);
    private static final TemplatePackagesAggregate TEMPLATE_PACKAGES_AGGREGATE = mock(TemplatePackagesAggregate.class);
    private static final ApplicationsAggregate APPLICATIONS_AGGREGATE = mock(ApplicationsAggregate.class);
    private static final ElasticSearchIndexationExecutor ELASTIC_SEARCH_INDEXATION_EXECUTOR = mock(ElasticSearchIndexationExecutor.class);

    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @ClassRule
    public static ResourceTestRule authResources = createAuthenticationResource(new HesperidesFullIndexationResource(
            ELASTIC_SEARCH_INDEXATION_EXECUTOR, APPLICATIONS_AGGREGATE, MODULES_AGGREGATE, TEMPLATE_PACKAGES_AGGREGATE));

    @Before
    public void setup() {
        reset(MODULES_AGGREGATE);
        reset(APPLICATIONS_AGGREGATE);
        reset(TEMPLATE_PACKAGES_AGGREGATE);
    }

    @Override
    protected ResourceTestRule getAuthResources() {
        return authResources;
    }

    @Test
    public void should_return_403_forbiden_when_clear_applications_caches() {
        assertThat(
            withNoTechAuth("/indexation/perform_reindex")
                    .post(Entity.json(null))
                    .getStatus()).isEqualTo(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void should_return_works_when_clear_applications_caches() {
        withTechAuth("/indexation/perform_reindex")
                .post(Entity.json(null));
    }
}
