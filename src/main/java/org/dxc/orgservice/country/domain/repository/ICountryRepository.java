package org.dxc.orgservice.country.domain.repository;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.Optional;
import java.util.UUID;

public interface ICountryRepository {
    void save(Country country);
    Optional<Country> findById(UUID id);
    boolean existsByName(OrgName name);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
