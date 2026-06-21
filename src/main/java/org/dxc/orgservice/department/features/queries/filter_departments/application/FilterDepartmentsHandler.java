package org.dxc.orgservice.department.features.queries.filter_departments.application;

import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.filter_departments.application.port.in.IFilterDepartmentsHandler;
import org.dxc.orgservice.department.features.queries.filter_departments.application.port.out.IFilterDepartmentsReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class FilterDepartmentsHandler implements IFilterDepartmentsHandler {

    private final IFilterDepartmentsReadModel readModel;

    public FilterDepartmentsHandler(IFilterDepartmentsReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public Page<DepartmentResponse> handle(FilterDepartmentsQuery query) {
        return readModel.findAll(query.campusId(), PageRequest.of(query.page(), query.size()));
    }
}
