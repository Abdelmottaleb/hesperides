package org.hesperides.application;

import org.assertj.core.api.Assertions;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.hesperides.application.exceptions.DuplicateModuleException;
import org.hesperides.domain.modules.commands.ModuleCommands;
import org.hesperides.domain.modules.entities.Module;
import org.hesperides.domain.modules.queries.ModuleAlreadyExistsQuery;
import org.hesperides.domain.modules.queries.ModuleByIdQuery;
import org.hesperides.domain.modules.queries.ModuleQueries;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class ModuleUseCasesTest {

    @Autowired
    ModuleUseCases useCases;

    @MockBean
    ModuleQueries queryGateway;

    @MockBean
    ModuleCommands commandGateway;

    @Test(expected = DuplicateModuleException.class)
    public void createWorkingCopy_should_fail_when_working_copy_already_exists() {

        Module.Key key = new Module.Key("x","1", Module.Type.workingcopy);

        given(queryGateway.moduleExist(any())).willReturn(true);
        given(commandGateway.createModule(any())).willReturn(key);

        useCases.createWorkingCopy(key);
    }

    @Test
    public void createWorkingCopy_should_pass_when_working_copy_do_not_exists() {

        Module.Key key = new Module.Key("x","1", Module.Type.workingcopy);

        given(queryGateway.moduleExist(any())).willReturn(false);
        given(commandGateway.createModule(any())).willReturn(key);

        useCases.createWorkingCopy(key);
    }

}