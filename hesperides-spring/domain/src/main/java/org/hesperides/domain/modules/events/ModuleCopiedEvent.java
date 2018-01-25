package org.hesperides.domain.modules.events;

import lombok.Value;
import org.hesperides.domain.modules.commands.Module;

@Value
public class ModuleCopiedEvent {

    Module.Key moduleKey;

    Module.Key sourceModuleKey;
}
