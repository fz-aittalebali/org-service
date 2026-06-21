package org.dxc.orgservice.department.features.queries.filter_departments.application.port.out;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IFilterDepartmentsReadModel {
    Page<DepartmentResponse> findAll(Optional<UUID> campusId, Pageable pageable);
}
