package org.dxc.orgservice.shared.query.pagination;

public record SortQuery(String field, String direction) {
    public SortQuery {
        if (field == null || field.isBlank()) field = "createdAt";
        if (direction == null || (!direction.equalsIgnoreCase("ASC") && !direction.equalsIgnoreCase("DESC"))) {
            direction = "ASC";
        }
    }
}
