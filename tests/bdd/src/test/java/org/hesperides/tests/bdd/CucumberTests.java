package org.hesperides.tests.bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import org.hesperides.HesperidesSpringApplication;
import org.hesperides.tests.bdd.commons.tools.HesperidesTestRestTemplate;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.hesperides.commons.spring.SpringProfiles.FAKE_MONGO;
import static org.hesperides.commons.spring.SpringProfiles.NOLDAP;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = {"classpath:org.hesperides.test.bdd"})
public class CucumberTests {
    /**
     * ces tests fonctionnent en mode "RANDOM_PORT", c'est à dire avec un serveur tomcat
     * démarré sur un port random.
     */
    @Configuration
    @SpringBootTest(classes = HesperidesSpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles(profiles = {FAKE_MONGO, NOLDAP})
    @ContextConfiguration
    public static class CucumberSpringBean {
        @Autowired
        protected HesperidesTestRestTemplate rest;

        @Before
        public void triggerSpringBootAppTestContextByCucumber() {
        }
    }

    public static void main(String[] args) {
        JUnitCore.main("CucumberTests");
    }
}
