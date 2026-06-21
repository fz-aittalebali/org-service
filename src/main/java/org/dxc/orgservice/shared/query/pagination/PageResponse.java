package org.dxc.orgservice.shared.query.pagination;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(PageResult<T> result) {
        return new PageResponse<>(
                result.content(),
                result.totalElements(),
                result.totalPages(),
                result.currentPage(),
                result.pageSize(),
                result.currentPage() == 0,
                result.currentPage() >= result.totalPages() - 1
        );
    }
}
