package org.dxc.orgservice.team.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class TeamUpdatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;
    private final UUID departmentId;

    public TeamUpdatedEvent(UUID teamId, String name, UUID departmentId, Instant occurredOn) {
        super(teamId, occurredOn);
        this.name = name;
        this.departmentId = departmentId;
    }

    public String getName() { return name; }
    public UUID getDepartmentId() { return departmentId; }
}
