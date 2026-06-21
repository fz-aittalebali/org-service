package org.dxc.orgservice.city.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CityCreatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;
    private final String zipCode;
    private final UUID countryId;

    public CityCreatedEvent(UUID cityId, String name, String zipCode, UUID countryId, Instant occurredOn) {
        super(cityId, occurredOn);
        this.name = name;
        this.zipCode = zipCode;
        this.countryId = countryId;
    }

    public String getName() { return name; }
    public String getZipCode() { return zipCode; }
    public UUID getCountryId() { return countryId; }
}
