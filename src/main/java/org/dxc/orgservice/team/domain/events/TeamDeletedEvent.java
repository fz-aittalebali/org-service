package org.dxc.orgservice.team.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class TeamDeletedEvent extends AbstractDomainEvent<UUID> {
    public TeamDeletedEvent(UUID teamId, Instant occurredOn) {
        super(teamId, occurredOn);
    }
}
