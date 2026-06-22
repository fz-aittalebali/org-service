package org.dxc.orgservice.shared.query.dtos;

import java.time.Instant;
import java.util.UUID;

public record UserSummaryDto(
        UUID    id,
        String  firstName,
        String  lastName,
        String  email,
        String  phone,
        String  status,
        Instant createdAt
) {}
