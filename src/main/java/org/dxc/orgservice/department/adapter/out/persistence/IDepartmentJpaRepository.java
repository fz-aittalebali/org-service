package org.dxc.orgservice.department.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IDepartmentJpaRepository extends JpaRepository<DepartmentJpaEntity, UUID> {
    boolean existsByNameAndCampusId(String name, UUID campusId);
    Page<DepartmentJpaEntity> findAllByCampusId(UUID campusId, Pageable pageable);
}
