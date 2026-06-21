package org.dxc.orgservice.country.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CountryUpdatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;

    public CountryUpdatedEvent(UUID countryId, String name, Instant occurredOn) {
        super(countryId, occurredOn);
        this.name = name;
    }

    public String getName() { return name; }
}
