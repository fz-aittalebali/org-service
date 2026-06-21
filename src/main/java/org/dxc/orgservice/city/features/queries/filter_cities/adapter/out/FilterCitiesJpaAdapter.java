package org.dxc.orgservice.city.features.queries.filter_cities.adapter.out;

import org.dxc.orgservice.city.adapter.out.persistence.ICityJpaRepository;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.filter_cities.application.port.out.IFilterCitiesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class FilterCitiesJpaAdapter implements IFilterCitiesReadModel {

    private final ICityJpaRepository jpaRepository;

    public FilterCitiesJpaAdapter(ICityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<CityResponse> findAll(Optional<UUID> countryId, Pageable pageable) {
        if (countryId.isPresent()) {
            return jpaRepository.findAllByCountryId(countryId.get(), pageable)
                    .map(e -> new CityResponse(e.getId(), e.getName(), e.getZipCode(), e.getCountryId(), e.getDomainCreatedAt()));
        }
        return jpaRepository.findAll(pageable)
                .map(e -> new CityResponse(e.getId(), e.getName(), e.getZipCode(), e.getCountryId(), e.getDomainCreatedAt()));
    }
}
