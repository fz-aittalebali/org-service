package org.dxc.orgservice.country.adapter.out.persistence;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class CountryWriteAdapter implements ICountryRepository {

    private final ICountryJpaRepository jpaRepository;
    private final CountryMapper mapper;

    public CountryWriteAdapter(ICountryJpaRepository jpaRepository, CountryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Country country) {
        jpaRepository.save(mapper.toJpaEntity(country));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Country> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(OrgName name) {
        return jpaRepository.existsByName(name.value());
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
