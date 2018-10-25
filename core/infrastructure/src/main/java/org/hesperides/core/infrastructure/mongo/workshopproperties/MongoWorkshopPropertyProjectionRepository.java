package org.hesperides.core.infrastructure.mongo.workshopproperties;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.core.domain.GetWorkshopPropertyQuery;
import org.hesperides.core.domain.WorkshopPropertyAlreadyExistsQuery;
import org.hesperides.core.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.core.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.core.domain.workshopproperties.WorkshopPropertyProjectionRepository;
import org.hesperides.core.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.hesperides.commons.spring.SpringProfiles.FAKE_MONGO;
import static org.hesperides.commons.spring.SpringProfiles.MONGO;

@Profile({MONGO, FAKE_MONGO})
@Repository
public class MongoWorkshopPropertyProjectionRepository implements WorkshopPropertyProjectionRepository {

    private final MongoWorkshopPropertyRepository workshopPropertyRepository;

    @Autowired
    public MongoWorkshopPropertyProjectionRepository(MongoWorkshopPropertyRepository workshopPropertyRepository) {
        this.workshopPropertyRepository = workshopPropertyRepository;
    }

    /*** QUERY HANDLERS ***/

    @QueryHandler
    @Override
    public Boolean onWorkshopPropertyAlreadyExistsQuery(WorkshopPropertyAlreadyExistsQuery query) {
        Optional<WorkshopPropertyDocument> workshopPropertyDocument = this.workshopPropertyRepository.findOptionalByKey(query.getKey());
        return workshopPropertyDocument.isPresent();
    }

    @QueryHandler
    @Override
    public Optional<WorkshopPropertyView> onGetWorkshopPropertyQuery(GetWorkshopPropertyQuery query) {
        Optional<WorkshopPropertyView> optionalWorkshopPropertyView = Optional.empty();
        Optional<WorkshopPropertyDocument> optionalWorkshopPropertyDocument = this.workshopPropertyRepository.findOptionalByKey(query.getKey());
        if (optionalWorkshopPropertyDocument.isPresent()) {
            optionalWorkshopPropertyView = Optional.of(optionalWorkshopPropertyDocument.get().toWorkshopPropertyView());
        }
        return optionalWorkshopPropertyView;
    }

    /*** EVENT HANDLERS ***/
    @EventHandler
    @Override
    public void onWorkshopPropertyCreatedEvent(WorkshopPropertyCreatedEvent event) {
        WorkshopPropertyDocument workshopPropertyDocument = new WorkshopPropertyDocument(event.getWorkshopProperty());
        this.workshopPropertyRepository.save(workshopPropertyDocument);

    }

    @EventHandler
    @Override
    public void onWorkshopPropertyUpdatedEvent(WorkshopPropertyUpdatedEvent event) {
        WorkshopPropertyDocument workshopPropertyDocument = new WorkshopPropertyDocument(event.getWorkshopProperty());
        this.workshopPropertyRepository.save(workshopPropertyDocument);
    }
}
