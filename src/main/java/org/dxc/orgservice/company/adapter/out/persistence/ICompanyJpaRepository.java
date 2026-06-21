package org.dxc.orgservice.company.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICompanyJpaRepository extends JpaRepository<CompanyJpaEntity, UUID> {
    boolean existsByName(String name);
    Page<CompanyJpaEntity> findAll(Pageable pageable);
}
