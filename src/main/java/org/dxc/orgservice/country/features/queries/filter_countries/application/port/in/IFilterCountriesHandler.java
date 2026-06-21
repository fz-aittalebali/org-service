package org.dxc.orgservice.country.features.queries.filter_countries.application.port.in;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.filter_countries.application.FilterCountriesQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.dxc.orgservice.shared.query.pagination.PageResult;

public interface IFilterCountriesHandler extends IQueryHandler<FilterCountriesQuery, PageResult<CountryResponse>> {}
