package org.hesperides.infrastructure.modules.local;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.hesperides.domain.Module;
import org.hesperides.domain.ModuleSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LocalModuleRepository implements ModuleSearchRepository {

    private static final Map<String, Module> MODULE_MAP = Maps.newHashMap();

    public List<Module> getModules() {
        return ImmutableList.copyOf(MODULE_MAP.values());
    }

}
