package org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.port.in;

import org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.GetUsersByCampusQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;

public interface IGetUsersByCampusHandler
        extends IQueryHandler<GetUsersByCampusQuery, UserPageResponse> {}
