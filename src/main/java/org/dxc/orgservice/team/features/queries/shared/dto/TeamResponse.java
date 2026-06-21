package org.dxc.orgservice.team.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record TeamResponse(UUID id, String name, UUID departmentId, Instant createdAt) {}
