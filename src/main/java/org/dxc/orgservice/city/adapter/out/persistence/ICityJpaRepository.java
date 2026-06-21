package org.dxc.orgservice.city.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICityJpaRepository extends JpaRepository<CityJpaEntity, UUID> {
    boolean existsByNameAndCountryId(String name, UUID countryId);
    Page<CityJpaEntity> findAllByCountryId(UUID countryId, Pageable pageable);
    Page<CityJpaEntity> findAll(Pageable pageable);
}
