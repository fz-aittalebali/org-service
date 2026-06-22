package org.dxc.orgservice.department.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IDepartmentJpaRepository extends JpaRepository<DepartmentJpaEntity, UUID> {
    boolean existsByNameAndCampusId(String name, UUID campusId);
    Page<DepartmentJpaEntity> findAllByCampusId(UUID campusId, Pageable pageable);

    @Query("SELECT d.id FROM DepartmentJpaEntity d WHERE d.campusId = :campusId")
    List<UUID> findIdsByCampusId(@Param("campusId") UUID campusId);
}
