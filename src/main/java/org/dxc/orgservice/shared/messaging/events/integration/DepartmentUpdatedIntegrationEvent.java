package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record DepartmentUpdatedIntegrationEvent(UUID departmentId, String name, UUID campusId, Instant occurredOn) {}
