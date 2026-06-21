package org.dxc.orgservice.country.features.queries.get_country_by_id.adapter.out;

import org.dxc.orgservice.country.adapter.out.persistence.ICountryJpaRepository;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.out.IGetCountryByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetCountryByIdJpaAdapter implements IGetCountryByIdReadModel {

    private final ICountryJpaRepository jpaRepository;

    public GetCountryByIdJpaAdapter(ICountryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CountryResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new CountryResponse(e.getId(), e.getName(), e.getDomainCreatedAt()));
    }
}
