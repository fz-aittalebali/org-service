package org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.in;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.GetCityByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetCityByIdHandler extends IQueryHandler<GetCityByIdQuery, CityResponse> {}
