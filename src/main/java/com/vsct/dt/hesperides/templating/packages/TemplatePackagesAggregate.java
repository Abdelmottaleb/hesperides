package com.vsct.dt.hesperides.templating.packages;

import com.google.common.eventbus.EventBus;
import com.vsct.dt.hesperides.HesperidesCacheParameter;
import com.vsct.dt.hesperides.HesperidesConfiguration;
import com.vsct.dt.hesperides.storage.EventStore;
import com.vsct.dt.hesperides.storage.UserProvider;
import com.vsct.dt.hesperides.templating.models.Models;
import com.vsct.dt.hesperides.templating.modules.template.TemplateRegistryInterface;

/**
 * Service used to manage templates as "packs".
 * There is no object representing the pack.
 * Templates belong to the same "TemplatePackage" through namespacing
 * Created by william_montaz on 24/11/2014.
 */
public class TemplatePackagesAggregate extends AbstractTemplatePackagesAggregate {
    /**
     * Internal structure holding in memory state
     */
    private TemplateRegistryInterface templateRegistry;

    /**
     * Helper class used to return a template model
     */
    private Models           models;

    /**
     * Nb event before store cache for force cache system.
     */
    private long nbEventBeforePersiste;

    /**
     * Constructor using no UserProvider (used when no loggin was possible)
     * @param eventBus  The {@link com.google.common.eventbus.EventBus} used to propagate events to other part of the application
     * @param eventStore The {@link com.vsct.dt.hesperides.storage.EventStore} used to store events
     * @param hesperidesConfiguration hesperides configuration
     */
    public TemplatePackagesAggregate(final EventBus eventBus, final EventStore eventStore,
                                     final HesperidesConfiguration hesperidesConfiguration) {
        super(eventBus, eventStore);

        initTemplateAggregate(eventStore, hesperidesConfiguration);
    }

    /**
     * Constructor using a specific UserProvider
     * @param eventBus The {@link com.google.common.eventbus.EventBus} used to propagate events to other part of the application
     * @param eventStore The {@link com.vsct.dt.hesperides.storage.EventStore} used to store events
     * @param userProvider A {@link com.vsct.dt.hesperides.storage.UserProvider} that indicates which user is performing the request
     * @param hesperidesConfiguration Hesperides configuration
     */
    public TemplatePackagesAggregate(final EventBus eventBus, final EventStore eventStore,
                                     final UserProvider userProvider,
                                     final HesperidesConfiguration hesperidesConfiguration) {
        super(eventBus, eventStore, userProvider);

        initTemplateAggregate(eventStore, hesperidesConfiguration);
    }

    /**
     * Init module.
     *
     * @param eventStore {@link com.vsct.dt.hesperides.storage.EventStore} used to store events
     * @param hesperidesConfiguration {@link com.vsct.dt.hesperides.HesperidesConfiguration} configuration hesperides
     */
    private void initTemplateAggregate(final EventStore eventStore,
                                       final HesperidesConfiguration hesperidesConfiguration) {
        HesperidesCacheParameter templateParameter = null;

        if (hesperidesConfiguration.getCacheConfiguration() != null) {
            templateParameter = hesperidesConfiguration.getCacheConfiguration().getTemplatePackage();
        }

        this.nbEventBeforePersiste
                = hesperidesConfiguration.getCacheConfiguration().getNbEventBeforePersiste();

        this.templateRegistry = new TemplatePackageRegistry(eventStore, nbEventBeforePersiste
                , templateParameter);
        this.models = new Models(this.templateRegistry);
    }

    @Override
    protected TemplateRegistryInterface getTemplateRegistry() {
        return this.templateRegistry;
    }

    @Override
    protected Models getModels() {
        return this.models;
    }

    public void removeFromCache(final String name, final String version, final boolean isWorkingCopy) {
        this.templateRegistry.removeFromCache(new TemplatePackageKey(name, version, isWorkingCopy));
    }

    public void removeAllCache() {
        this.templateRegistry.removeAllCache();
    }

    @Override
    public void regenerateCache() {
        // Nothing
    }
}
