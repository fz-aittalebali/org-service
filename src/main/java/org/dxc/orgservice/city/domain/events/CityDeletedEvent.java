package org.dxc.orgservice.city.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CityDeletedEvent extends AbstractDomainEvent<UUID> {
    public CityDeletedEvent(UUID cityId, Instant occurredOn) {
        super(cityId, occurredOn);
    }
}
