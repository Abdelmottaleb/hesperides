/*
 *
 * This file is part of the Hesperides distribution.
 * (https://github.com/voyages-sncf-technologies/hesperides)
 * Copyright (c) 2016 VSCT.
 *
 * Hesperides is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3.
 *
 * Hesperides is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.hesperides.core.domain.workshopproperties.commands;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.hesperides.core.domain.CreateWorkshopPropertyCommand;
import org.hesperides.core.domain.UpdateWorkshopPropertyCommand;
import org.hesperides.core.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.core.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.core.domain.workshopproperties.entities.WorkshopProperty;

import java.io.Serializable;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.isLive;

@Slf4j
@Aggregate
@NoArgsConstructor
public class WorkshopPropertyAggregate implements Serializable {

    @AggregateIdentifier
    private String key;

    /*** COMMAND HANDLERS ***/
    @CommandHandler
    public WorkshopPropertyAggregate(CreateWorkshopPropertyCommand command) {
        log.debug("Applying createWorkshopPropertyCommand...");

        //pré-traitement avant d'envoi dans l'EventBus
        WorkshopProperty workshopProperty = command.getWorkshopProperty()
                .concatKeyValue();
        apply(new WorkshopPropertyCreatedEvent(workshopProperty, command.getUser()));
    }

    @CommandHandler
    public void onUpdateWorkshopPropertyCommand(UpdateWorkshopPropertyCommand command) {
        log.debug("Applying UpdateWorkshopPropertyCommand...");

        WorkshopProperty workshopProperty = command.getWorkshopProperty()
                .concatKeyValue();

        //si tout est OK, la commande est validé (validation des données + pré traitement des données), on publie l'event sur l'EventBus => provoquer / déclancher l'event
        apply(new WorkshopPropertyUpdatedEvent(workshopProperty, command.getUser()));
    }

    /*** EVENT HANDLERS ***/
    @EventSourcingHandler
    public void onWorkshopPropertyCreatedEvent(WorkshopPropertyCreatedEvent event) {
        this.key = event.getWorkshopProperty().getKey();
        log.debug("WorkshopProperty created (aggregate is live ? {})", isLive());
    }

    @EventSourcingHandler
    public void onWorkshopPropertyUpdatedEvent(WorkshopPropertyUpdatedEvent event) {
        log.debug("WorkshopProperty Updated (aggregate is live ? {})", isLive());
    }
}