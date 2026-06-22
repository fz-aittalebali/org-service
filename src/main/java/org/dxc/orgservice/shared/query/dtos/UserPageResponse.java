package org.dxc.orgservice.shared.query.dtos;

import java.util.List;

public record UserPageResponse(
        List<UserSummaryDto> content,
        int                  page,
        int                  size,
        long                 totalElements,
        int                  totalPages,
        boolean              last
) {}
