package org.hesperides.tests.bdd.modules.scenarios;

import cucumber.api.java8.En;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;
import org.hesperides.presentation.io.TemplateIO;
import org.hesperides.tests.bdd.CucumberSpringBean;
import org.hesperides.tests.bdd.modules.contexts.ExistingTemplateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetTemplates extends CucumberSpringBean implements En {

    private ResponseEntity<TemplateIO[]> response;

    @Autowired
    private ExistingTemplateContext existingTemplate;

    public GetTemplates() {

        When("^retrieving those templates$", () -> {
            TemplateContainer.Key moduleKey = existingTemplate.getExistingModuleContext().getModuleKey();
            response = rest.getTestRest().getForEntity("/modules/{moduleName}/{moduleVersion}/{moduleType}/templates", TemplateIO[].class,
                    moduleKey.getName(), moduleKey.getVersion(), moduleKey.getVersionType());
        });

        Then("^the templates are retrieved$", () -> {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<TemplateIO> templateOutputs = Arrays.asList(response.getBody());
            assertEquals(6, templateOutputs.size());
            //TODO Vérifier le contenu de chaque template ?
        });
    }
}
