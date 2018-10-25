package org.hesperides.core.domain

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.hesperides.core.domain.security.User
import org.hesperides.core.domain.security.UserEvent
import org.hesperides.core.domain.workshopproperties.entities.WorkshopProperty

// Commands
data class CreateWorkshopPropertyCommand(val workshopProperty: WorkshopProperty, val user: User)

data class UpdateWorkshopPropertyCommand(@TargetAggregateIdentifier val key: String, val workshopProperty: WorkshopProperty, val user: User)


// Events
data class WorkshopPropertyCreatedEvent(val workshopProperty: WorkshopProperty, override val user: User) : UserEvent(user);
data class WorkshopPropertyUpdatedEvent(val workshopProperty: WorkshopProperty, override val user: User) : UserEvent(user)

// Queries
data class WorkshopPropertyAlreadyExistsQuery(val key: String)

data class GetWorkshopPropertyQuery(val key: String)
