package org.dxc.orgservice.shared.query.pagination;

import java.util.List;

public record PageResult<T>(List<T> content, long totalElements, int totalPages, int currentPage, int pageSize) {}
