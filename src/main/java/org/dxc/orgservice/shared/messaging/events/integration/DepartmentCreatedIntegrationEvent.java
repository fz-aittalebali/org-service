package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record DepartmentCreatedIntegrationEvent(UUID departmentId, String name, UUID campusId, Instant occurredOn) {}
