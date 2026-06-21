package org.dxc.orgservice.country.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CountryDeletedEvent extends AbstractDomainEvent<UUID> {
    public CountryDeletedEvent(UUID countryId, Instant occurredOn) {
        super(countryId, occurredOn);
    }
}
