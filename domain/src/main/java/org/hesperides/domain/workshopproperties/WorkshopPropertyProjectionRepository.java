package org.hesperides.domain.workshopproperties;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyExistsQuery;

public interface WorkshopPropertyProjectionRepository {

    /*** EVENT HANDLERS ***/

    @EventSourcingHandler
    void on(WorkshopPropertyCreatedEvent event);

    /*** QUERY HANDLERS ***/

    @QueryHandler
    Boolean query(WorkshopPropertyExistsQuery query);
}
