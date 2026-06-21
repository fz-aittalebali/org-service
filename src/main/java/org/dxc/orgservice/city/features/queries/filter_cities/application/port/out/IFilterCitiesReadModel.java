package org.dxc.orgservice.city.features.queries.filter_cities.application.port.out;

import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IFilterCitiesReadModel {
    Page<CityResponse> findAll(Optional<UUID> countryId, Pageable pageable);
}
