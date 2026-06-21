package org.dxc.orgservice.campus.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CampusDeletedEvent extends AbstractDomainEvent<UUID> {
    public CampusDeletedEvent(UUID campusId, Instant occurredOn) {
        super(campusId, occurredOn);
    }
}
