package org.dxc.orgservice.shared.messaging.events.integration;

import java.time.Instant;
import java.util.UUID;

public record CityCreatedIntegrationEvent(UUID cityId, String name, String zipCode, UUID countryId, Instant occurredOn) {}
