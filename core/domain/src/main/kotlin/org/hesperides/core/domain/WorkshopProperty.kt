package org.hesperides.domain

import org.hesperides.domain.security.User
import org.hesperides.domain.security.UserEvent
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty

// Command
data class CreateWorkshopPropertyCommand(val workshopProperty: WorkshopProperty, val user: User)

// Event
data class WorkshopPropertyCreatedEvent(val workshopProperty: WorkshopProperty, override val user: User) : UserEvent(user)

// Query
data class WorkshopPropertyExistsQuery(val key: String)
data class GetWorkshopPropertyByKeyQuery(val key: String)