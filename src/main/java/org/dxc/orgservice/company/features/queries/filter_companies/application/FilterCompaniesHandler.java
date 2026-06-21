package org.dxc.orgservice.company.features.queries.filter_companies.application;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.filter_companies.application.port.in.IFilterCompaniesHandler;
import org.dxc.orgservice.company.features.queries.filter_companies.application.port.out.IFilterCompaniesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class FilterCompaniesHandler implements IFilterCompaniesHandler {

    private final IFilterCompaniesReadModel readModel;

    public FilterCompaniesHandler(IFilterCompaniesReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public Page<CompanyResponse> handle(FilterCompaniesQuery query) {
        return readModel.findAll(PageRequest.of(query.page(), query.size()));
    }
}
