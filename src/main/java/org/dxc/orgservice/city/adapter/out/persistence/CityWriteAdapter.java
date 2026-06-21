package org.dxc.orgservice.city.adapter.out.persistence;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class CityWriteAdapter implements ICityRepository {

    private final ICityJpaRepository jpaRepository;
    private final CityMapper mapper;

    public CityWriteAdapter(ICityJpaRepository jpaRepository, CityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(City city) {
        jpaRepository.save(mapper.toJpaEntity(city));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<City> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCountryId(OrgName name, CountryId countryId) {
        return jpaRepository.existsByNameAndCountryId(name.value(), countryId.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
