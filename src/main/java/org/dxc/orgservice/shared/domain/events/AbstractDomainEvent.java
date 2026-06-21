package org.dxc.orgservice.shared.domain.events;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractDomainEvent<ID> implements IDomainEvent {

    private final UUID eventId = UUID.randomUUID();
    private final ID aggregateRootId;
    private final Instant occurredOn;

    protected AbstractDomainEvent(ID aggregateRootId, Instant occurredOn) {
        this.aggregateRootId = aggregateRootId;
        this.occurredOn = occurredOn;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    public ID getAggregateRootId() {
        return aggregateRootId;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }
}
