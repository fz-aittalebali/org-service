package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record TeamCreatedIntegrationEvent(UUID teamId, String name, UUID departmentId, Instant occurredOn) {}
