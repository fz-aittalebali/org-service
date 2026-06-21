package org.dxc.orgservice.city.features.queries.filter_cities.application.port.in;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.filter_cities.application.FilterCitiesQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.springframework.data.domain.Page;

public interface IFilterCitiesHandler extends IQueryHandler<FilterCitiesQuery, Page<CityResponse>> {}
