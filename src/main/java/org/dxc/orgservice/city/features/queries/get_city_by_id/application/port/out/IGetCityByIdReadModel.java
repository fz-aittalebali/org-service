package org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.out;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetCityByIdReadModel {
    Optional<CityResponse> findById(UUID id);
}
