package org.hesperides.domain.modules.events;

import lombok.Value;
import org.hesperides.domain.modules.Module;

@Value
public class ModuleCreatedEvent implements ModuleEvent {
    Module.Key moduleKey;
}
