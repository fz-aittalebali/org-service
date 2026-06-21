package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record CampusUpdatedIntegrationEvent(UUID campusId, String name, UUID companyId, UUID cityId, Instant occurredOn) {}
