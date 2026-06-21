package org.dxc.orgservice.company.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CompanyDeletedEvent extends AbstractDomainEvent<UUID> {
    public CompanyDeletedEvent(UUID companyId, Instant occurredOn) {
        super(companyId, occurredOn);
    }
}
