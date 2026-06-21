package org.dxc.orgservice.city.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record CityResponse(UUID id, String name, String zipCode, UUID countryId, Instant createdAt) {}
