package org.hesperides.domain.modules.commands;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.hesperides.domain.modules.entities.Module;
import org.hesperides.domain.modules.entities.Template;

@Value
public class UpdateTemplateCommand {
    @TargetAggregateIdentifier
    Module.Key moduleKey;
    Template template;
}
