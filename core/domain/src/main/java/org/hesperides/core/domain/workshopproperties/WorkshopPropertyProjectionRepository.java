package org.hesperides.core.domain.workshopproperties;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.core.domain.GetWorkshopPropertyQuery;
import org.hesperides.core.domain.WorkshopPropertyAlreadyExistsQuery;
import org.hesperides.core.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.core.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.core.domain.workshopproperties.queries.views.WorkshopPropertyView;

import java.util.Optional;

public interface WorkshopPropertyProjectionRepository {


    /*** QUERY HANDLERS ***/

    @QueryHandler
    Boolean onWorkshopPropertyAlreadyExistsQuery(WorkshopPropertyAlreadyExistsQuery query);

    @QueryHandler
    Optional<WorkshopPropertyView> onGetWorkshopPropertyQuery(GetWorkshopPropertyQuery query);

    /*** EVENT HANDLERS ***/
    @EventHandler
    void onWorkshopPropertyCreatedEvent(WorkshopPropertyCreatedEvent event);

    @EventHandler
    void onWorkshopPropertyUpdatedEvent(WorkshopPropertyUpdatedEvent event);

}
