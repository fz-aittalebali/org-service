package org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.in;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.GetDepartmentByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetDepartmentByIdHandler extends IQueryHandler<GetDepartmentByIdQuery, DepartmentResponse> {}
