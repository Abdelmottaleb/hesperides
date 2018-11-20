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
package org.hesperides.tests.bdd.files.scenarios;

import cucumber.api.java8.En;
import org.hesperides.core.presentation.io.ModuleIO;
import org.hesperides.core.presentation.io.files.InstanceFileOutput;
import org.hesperides.core.presentation.io.platforms.DeployedModuleIO;
import org.hesperides.core.presentation.io.platforms.PlatformIO;
import org.hesperides.core.presentation.io.templatecontainers.TemplateIO;
import org.hesperides.tests.bdd.commons.HesperidesScenario;
import org.hesperides.tests.bdd.files.FileClient;
import org.hesperides.tests.bdd.modules.ModuleBuilder;
import org.hesperides.tests.bdd.platforms.PlatformBuilder;
import org.hesperides.tests.bdd.templatecontainers.builders.TemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetFiles extends HesperidesScenario implements En {

    @Autowired
    private FileClient fileClient;
    @Autowired
    private PlatformBuilder platformBuilder;
    @Autowired
    private ModuleBuilder moduleBuilder;
    @Autowired
    private TemplateBuilder templateBuilder;

    private List<InstanceFileOutput> instanceFiles;

    public GetFiles() {

        When("^I( try to)? get the (instance|module)? files$", (String tryTo, String instanceOrModuleFiles) -> {
            PlatformIO platform = platformBuilder.buildInput();
            ModuleIO module = moduleBuilder.build();
            TemplateIO template = templateBuilder.build();

            DeployedModuleIO deployedModule = CollectionUtils.isEmpty(platform.getDeployedModules()) ? null : platform.getDeployedModules().get(0);
            String modulePath = deployedModule != null ? deployedModule.getPath() : "anything";
            boolean simulate = "module".equals(instanceOrModuleFiles);
            String instanceName = getInstanceName(deployedModule, simulate);

            testContext.responseEntity = fileClient.getInstanceFiles(
                    platform.getApplicationName(),
                    platform.getPlatformName(),
                    modulePath,
                    module.getName(),
                    module.getVersion(),
                    instanceName,
                    module.getIsWorkingCopy(),
                    simulate,
                    HesperidesScenario.getResponseType(tryTo, InstanceFileOutput[].class));

            instanceFiles = Arrays.asList(
                    new InstanceFileOutput(
                            template.getLocation() + "/" + template.getFilename(),
                            "/rest/files"
                                    + "/applications/" + platform.getApplicationName()
                                    + "/platforms/" + platform.getPlatformName()
                                    + "/" + modulePath
                                    + "/" + module.getName()
                                    + "/" + module.getVersion()
                                    + "/instances/" + instanceName
                                    + "/" + template.getName()
                                    + "?isWorkingCopy=" + module.getIsWorkingCopy()
                                    + "&template_namespace=" + moduleBuilder.getNamespace()
                                    + "&simulate=" + simulate,
                            new InstanceFileOutput.Rights("   ", "   ", "   ")
                    )
            );
        });

        Then("^the files are successfully retrieved$", () -> {
            assertOK();
            List<InstanceFileOutput> expectedOutput = instanceFiles;
            List<InstanceFileOutput> actualOutput = Arrays.asList(getBodyAsArray());
            assertEquals(expectedOutput, actualOutput);
            //TODO Vérifier qu'on obtient un code 200 lorsqu'on tente de récupérer chaque fichier listé dans le résultat précédent
        });
    }

    private String getInstanceName(DeployedModuleIO deployedModule, boolean simulate) {
        return deployedModule == null || CollectionUtils.isEmpty(deployedModule.getInstances()) || simulate ? "anything" : deployedModule.getInstances().get(0).getName();
    }
}
