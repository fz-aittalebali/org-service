package org.dxc.orgservice.department.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record DepartmentResponse(UUID id, String name, UUID campusId, Instant createdAt) {}
