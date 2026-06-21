package org.dxc.orgservice.department.features.queries.filter_departments.application.port.in;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.filter_departments.application.FilterDepartmentsQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.springframework.data.domain.Page;

public interface IFilterDepartmentsHandler extends IQueryHandler<FilterDepartmentsQuery, Page<DepartmentResponse>> {}
