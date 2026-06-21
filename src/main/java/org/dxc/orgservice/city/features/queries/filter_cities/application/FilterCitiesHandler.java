package org.dxc.orgservice.city.features.queries.filter_cities.application;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.filter_cities.application.port.in.IFilterCitiesHandler;
import org.dxc.orgservice.city.features.queries.filter_cities.application.port.out.IFilterCitiesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class FilterCitiesHandler implements IFilterCitiesHandler {

    private final IFilterCitiesReadModel readModel;

    public FilterCitiesHandler(IFilterCitiesReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public Page<CityResponse> handle(FilterCitiesQuery query) {
        return readModel.findAll(query.countryId(), PageRequest.of(query.page(), query.size()));
    }
}
