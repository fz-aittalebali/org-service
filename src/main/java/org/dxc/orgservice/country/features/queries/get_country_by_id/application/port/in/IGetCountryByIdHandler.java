package org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.in;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.GetCountryByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetCountryByIdHandler extends IQueryHandler<GetCountryByIdQuery, CountryResponse> {}
