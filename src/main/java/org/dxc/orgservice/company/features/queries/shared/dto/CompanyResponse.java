package org.dxc.orgservice.company.features.queries.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record CompanyResponse(UUID id, String name, Instant createdAt) {}
