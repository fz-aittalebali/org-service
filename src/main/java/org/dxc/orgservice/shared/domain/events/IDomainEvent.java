package org.dxc.orgservice.shared.domain.events;

import java.time.Instant;
import java.util.UUID;

public interface IDomainEvent {
    UUID getEventId();
    Instant getOccurredOn();
}
