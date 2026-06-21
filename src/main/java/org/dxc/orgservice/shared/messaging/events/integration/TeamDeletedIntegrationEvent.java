package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record TeamDeletedIntegrationEvent(UUID teamId, Instant occurredOn) {}
