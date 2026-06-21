package org.dxc.orgservice.campus.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record CampusResponse(UUID id, String name, UUID companyId, UUID cityId, Instant createdAt) {}
