package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record CompanyUpdatedIntegrationEvent(UUID companyId, String name, Instant occurredOn) {}
