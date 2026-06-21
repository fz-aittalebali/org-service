package org.dxc.orgservice.country.features.queries.filter_countries.application.port.out;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.shared.query.pagination.PageResult;
import org.dxc.orgservice.shared.query.pagination.SortQuery;

public interface IFilterCountriesReadModel {
    PageResult<CountryResponse> findAll(PageQuery page, SortQuery sort);
}
