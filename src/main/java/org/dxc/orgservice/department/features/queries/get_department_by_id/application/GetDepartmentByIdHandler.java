package org.dxc.orgservice.department.features.queries.get_department_by_id.application;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.in.IGetDepartmentByIdHandler;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.out.IGetDepartmentByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetDepartmentByIdHandler implements IGetDepartmentByIdHandler {

    private final IGetDepartmentByIdReadModel readModel;

    public GetDepartmentByIdHandler(IGetDepartmentByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public DepartmentResponse handle(GetDepartmentByIdQuery query) {
        return readModel.findById(query.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", query.departmentId()));
    }
}
