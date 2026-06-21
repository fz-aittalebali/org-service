package org.dxc.orgservice.team.features.queries.shared.dto;

import java.util.UUID;

public record TeamHierarchyResponse(
        UUID teamId,
        String teamName,
        UUID departmentId,
        String departmentName,
        UUID campusId,
        String campusName,
        UUID companyId,
        String companyName,
        UUID cityId,
        String cityName,
        UUID countryId,
        String countryName
) {}
