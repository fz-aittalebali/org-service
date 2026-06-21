package org.dxc.orgservice.country.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record CountryResponse(UUID id, String name, Instant createdAt) {}
