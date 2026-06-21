package org.dxc.orgservice.city.features.queries.get_city_by_id.application;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.in.IGetCityByIdHandler;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.out.IGetCityByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetCityByIdHandler implements IGetCityByIdHandler {

    private final IGetCityByIdReadModel readModel;

    public GetCityByIdHandler(IGetCityByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public CityResponse handle(GetCityByIdQuery query) {
        return readModel.findById(query.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", query.cityId()));
    }
}
