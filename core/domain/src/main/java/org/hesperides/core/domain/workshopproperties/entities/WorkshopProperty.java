package org.hesperides.core.domain.workshopproperties.entities;

import lombok.Value;

@Value
public class WorkshopProperty {
    String key;
    String value;
    String keyValue;

    public WorkshopProperty concatKeyValue() {
        return new WorkshopProperty(key, value, key + value);

    }
}