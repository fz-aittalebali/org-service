package org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.out;

import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetCountryByIdReadModel {
    Optional<CountryResponse> findById(UUID id);
}
