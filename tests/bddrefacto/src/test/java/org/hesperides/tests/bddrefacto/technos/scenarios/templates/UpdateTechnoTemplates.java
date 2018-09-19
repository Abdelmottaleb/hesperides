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
package org.hesperides.tests.bddrefacto.technos.scenarios.templates;

import cucumber.api.java8.En;
import org.hesperides.core.presentation.io.templatecontainers.TemplateIO;
import org.hesperides.tests.bddrefacto.technos.TechnoBuilder;
import org.hesperides.tests.bddrefacto.technos.TechnoClient;
import org.hesperides.tests.bddrefacto.templatecontainers.TemplateBuilder;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UpdateTechnoTemplates implements En {

    @Autowired
    private TemplateBuilder templateBuilder;
    @Autowired
    private TechnoBuilder technoBuilder;
    @Autowired
    private TechnoClient technoClient;

    private ResponseEntity responseEntity;

    public UpdateTechnoTemplates() {

        Given("^a template to update$", () -> {
            templateBuilder = new TemplateBuilder();
            templateBuilder.withVersionId(1);
        });

        Given("^a template that doesn't exist in this techno$", () -> {
            templateBuilder = new TemplateBuilder();
            templateBuilder.withName("nope");
        });

        Given("^a template with an outdated version$", () -> {
            templateBuilder = new TemplateBuilder();
            templateBuilder.withVersionId(0);
        });

        When("^I update this techno template$", () -> {
            responseEntity = technoClient.updateTemplate(templateBuilder.build(), technoBuilder.build(), TemplateIO.class);
        });

        When("^I try to update this techno template$", () -> {
            responseEntity = technoClient.updateTemplate(templateBuilder.build(), technoBuilder.build(), String.class);
        });

        Then("^the techno template is successfully updated$", () -> {
            Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        });

        Then("^the techno template update is rejected with a not found error$", () -> {
            Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        });

        Then("^the techno template update is rejected with a conflict error$", () -> {
            Assert.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        });
    }
}
