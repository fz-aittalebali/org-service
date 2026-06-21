package org.dxc.orgservice.shared.application.ports.out;

import java.time.Instant;
import java.util.UUID;

public record OutboxEvent(
        UUID id,
        String eventType,
        String topic,
        String aggregateId,
        String payload,
        Instant occurredOn
) {}
