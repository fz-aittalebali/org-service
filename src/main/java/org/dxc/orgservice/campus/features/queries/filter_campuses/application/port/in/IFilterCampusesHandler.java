package org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.in;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.FilterCampusesQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.springframework.data.domain.Page;

public interface IFilterCampusesHandler extends IQueryHandler<FilterCampusesQuery, Page<CampusResponse>> {}
