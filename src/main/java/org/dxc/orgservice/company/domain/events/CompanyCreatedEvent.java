package org.dxc.orgservice.company.domain.events;

import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class CompanyCreatedEvent extends AbstractDomainEvent<UUID> {
    private final String name;

    public CompanyCreatedEvent(UUID companyId, String name, Instant occurredOn) {
        super(companyId, occurredOn);
        this.name = name;
    }

    public String getName() { return name; }
}
