package org.dxc.orgservice.department.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class DepartmentCreatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;
    private final UUID campusId;

    public DepartmentCreatedEvent(UUID departmentId, String name, UUID campusId, Instant occurredOn) {
        super(departmentId, occurredOn);
        this.name = name;
        this.campusId = campusId;
    }

    public String getName() { return name; }
    public UUID getCampusId() { return campusId; }
}
