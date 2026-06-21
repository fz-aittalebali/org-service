package org.dxc.orgservice.campus.features.queries.filter_campuses.application;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.in.IFilterCampusesHandler;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.out.IFilterCampusesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class FilterCampusesHandler implements IFilterCampusesHandler {

    private final IFilterCampusesReadModel readModel;

    public FilterCampusesHandler(IFilterCampusesReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public Page<CampusResponse> handle(FilterCampusesQuery query) {
        return readModel.findAll(query.companyId(), PageRequest.of(query.page(), query.size()));
    }
}
