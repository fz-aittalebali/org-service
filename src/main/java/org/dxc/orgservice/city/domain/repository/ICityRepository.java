package org.dxc.orgservice.city.domain.repository;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.Optional;
import java.util.UUID;

public interface ICityRepository {
    void save(City city);
    Optional<City> findById(UUID id);
    boolean existsByNameAndCountryId(OrgName name, CountryId countryId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
