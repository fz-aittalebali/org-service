package org.dxc.orgservice.department.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class DepartmentDeletedEvent extends AbstractDomainEvent<UUID> {
    public DepartmentDeletedEvent(UUID departmentId, Instant occurredOn) {
        super(departmentId, occurredOn);
    }
}
