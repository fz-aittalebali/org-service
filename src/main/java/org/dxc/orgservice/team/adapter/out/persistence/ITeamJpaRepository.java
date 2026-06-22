package org.dxc.orgservice.team.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITeamJpaRepository extends JpaRepository<TeamJpaEntity, UUID> {
    boolean existsByNameAndDepartmentId(String name, UUID departmentId);
    Page<TeamJpaEntity> findAllByDepartmentId(UUID departmentId, Pageable pageable);

    @Query(value = """
            SELECT t.id          AS teamId,
                   t.name        AS teamName,
                   d.id          AS departmentId,
                   d.name        AS departmentName,
                   ca.id         AS campusId,
                   ca.name       AS campusName,
                   co.id         AS companyId,
                   co.name       AS companyName,
                   ci.id         AS cityId,
                   ci.name       AS cityName,
                   cn.id         AS countryId,
                   cn.name       AS countryName
            FROM teams t
            JOIN departments d  ON d.id  = t.department_id
            JOIN campuses ca    ON ca.id = d.campus_id
            JOIN companies co   ON co.id = ca.company_id
            JOIN cities ci      ON ci.id = ca.city_id
            JOIN countries cn   ON cn.id = ci.country_id
            WHERE t.id = :teamId
            """, nativeQuery = true)
    Optional<TeamHierarchyProjection> findHierarchyByTeamId(@Param("teamId") UUID teamId);

    @Query("SELECT t.id FROM TeamJpaEntity t WHERE t.departmentId IN :deptIds")
    List<UUID> findIdsByDepartmentIdIn(@Param("deptIds") Collection<UUID> deptIds);
}
