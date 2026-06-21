package org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.in;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.GetCampusByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetCampusByIdHandler extends IQueryHandler<GetCampusByIdQuery, CampusResponse> {}
