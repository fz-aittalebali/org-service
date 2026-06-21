package org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.out;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetDepartmentByIdReadModel {
    Optional<DepartmentResponse> findById(UUID id);
}
