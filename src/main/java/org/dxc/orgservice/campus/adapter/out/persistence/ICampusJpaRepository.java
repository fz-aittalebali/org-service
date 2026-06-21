package org.dxc.orgservice.campus.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICampusJpaRepository extends JpaRepository<CampusJpaEntity, UUID> {
    boolean existsByNameAndCompanyId(String name, UUID companyId);
    Page<CampusJpaEntity> findAllByCompanyId(UUID companyId, Pageable pageable);
}
