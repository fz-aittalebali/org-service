package org.dxc.orgservice.campus.features.queries.get_users_by_campus.application;

import org.dxc.orgservice.shared.query.pagination.PageQuery;

import java.util.UUID;

public record GetUsersByCampusQuery(
        UUID      campusId,
        String    role,
        PageQuery pageQuery
) {}
