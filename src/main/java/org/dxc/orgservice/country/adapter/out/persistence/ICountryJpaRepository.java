package org.dxc.orgservice.country.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICountryJpaRepository extends JpaRepository<CountryJpaEntity, UUID> {
    boolean existsByName(String name);
    Page<CountryJpaEntity> findAll(Pageable pageable);
}
