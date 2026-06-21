package org.dxc.orgservice.country.features.queries.filter_countries.application;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.filter_countries.application.port.in.IFilterCountriesHandler;
import org.dxc.orgservice.country.features.queries.filter_countries.application.port.out.IFilterCountriesReadModel;
import org.dxc.orgservice.shared.query.pagination.PageResult;

public class FilterCountriesHandler implements IFilterCountriesHandler {

    private final IFilterCountriesReadModel readModel;

    public FilterCountriesHandler(IFilterCountriesReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public PageResult<CountryResponse> handle(FilterCountriesQuery query) {
        return readModel.findAll(query.page(), query.sort());
    }
}
