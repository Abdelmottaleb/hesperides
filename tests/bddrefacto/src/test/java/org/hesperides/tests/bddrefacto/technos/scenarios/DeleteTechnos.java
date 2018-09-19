package org.hesperides.tests.bddrefacto.technos.scenarios;

import cucumber.api.java8.En;
import org.hesperides.core.presentation.io.TechnoIO;
import org.hesperides.tests.bddrefacto.commons.StepHelper;
import org.hesperides.tests.bddrefacto.technos.TechnoBuilder;
import org.hesperides.tests.bddrefacto.technos.TechnoClient;
import org.hesperides.tests.bddrefacto.templatecontainers.TemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class DeleteTechnos implements En {

    @Autowired
    private TechnoBuilder technoBuilder;
    @Autowired
    private TemplateBuilder templateBuilder;
    @Autowired
    private TechnoClient technoClient;

    private ResponseEntity responseEntity;

    public DeleteTechnos() {

        When("^I( try to)? delete this techno$", (final String tryTo) -> {
            responseEntity = technoClient.delete(technoBuilder.build(), StepHelper.getResponseType(tryTo, ResponseEntity.class));
        });
        ;

        Then("^the techno is successfully deleted$", () -> {
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            responseEntity = technoClient.get(technoBuilder.build(), String.class);
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        });

        Then("^the techno deletion is rejected with a not found error$", () -> {
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        });
    }

    private String getExpectedNamespace(TechnoIO technoInput) {
        return "packages#" + technoInput.getName() + "#" + technoInput.getVersion() + "#" + (technoInput.isWorkingCopy() ? "WORKINGCOPY" : "RELEASE");
    }
}
