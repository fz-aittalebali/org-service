package org.dxc.orgservice.city.features.queries.get_city_by_id.adapter.out;

import org.dxc.orgservice.city.adapter.out.persistence.ICityJpaRepository;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.out.IGetCityByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetCityByIdJpaAdapter implements IGetCityByIdReadModel {

    private final ICityJpaRepository jpaRepository;

    public GetCityByIdJpaAdapter(ICityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CityResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new CityResponse(e.getId(), e.getName(), e.getZipCode(), e.getCountryId(), e.getDomainCreatedAt()));
    }
}
