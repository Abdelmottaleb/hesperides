package org.hesperides.infrastructure.mongo.workshopproperties;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyExistsQuery;
import org.hesperides.domain.workshopproperties.WorkshopPropertyProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.hesperides.domain.framework.Profiles.FAKE_MONGO;
import static org.hesperides.domain.framework.Profiles.MONGO;

@Profile({MONGO, FAKE_MONGO})
@Repository
public class MongoWorkshopPropertyProjectionRepository implements WorkshopPropertyProjectionRepository {

    private final MongoWorkshopPropertyRepository workshopPropertyRepository;

    @Autowired
    public MongoWorkshopPropertyProjectionRepository(MongoWorkshopPropertyRepository workshopPropertyRepository) {
        this.workshopPropertyRepository = workshopPropertyRepository;
    }

    /*** EVENT HANDLERS ***/

    @EventSourcingHandler
    @Override
    public void on(WorkshopPropertyCreatedEvent event) {
        WorkshopPropertyDocument workshopPropertyDocument = WorkshopPropertyDocument.fromDomainInstance(event.getWorkshopProperty());
        workshopPropertyRepository.save(workshopPropertyDocument);
    }

    /*** QUERY HANDLERS ***/

    @QueryHandler
    @Override
    public Boolean query(WorkshopPropertyExistsQuery query) {
        Optional<WorkshopPropertyDocument> optionalWorkshopPropertyDocument = workshopPropertyRepository.findOptionalByKey(query.getKey());
        return optionalWorkshopPropertyDocument.isPresent();
    }
}
