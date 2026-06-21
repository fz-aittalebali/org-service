package org.dxc.orgservice.department.domain.repository;

import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.Optional;
import java.util.UUID;

public interface IDepartmentRepository {
    void save(Department department);
    Optional<Department> findById(UUID id);
    boolean existsByNameAndCampusId(OrgName name, CampusId campusId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
