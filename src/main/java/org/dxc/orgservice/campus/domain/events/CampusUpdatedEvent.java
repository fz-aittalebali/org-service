package org.dxc.orgservice.campus.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CampusUpdatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;
    private final UUID companyId;
    private final UUID cityId;

    public CampusUpdatedEvent(UUID campusId, String name, UUID companyId, UUID cityId, Instant occurredOn) {
        super(campusId, occurredOn);
        this.name = name;
        this.companyId = companyId;
        this.cityId = cityId;
    }

    public String getName() { return name; }
    public UUID getCompanyId() { return companyId; }
    public UUID getCityId() { return cityId; }
}
