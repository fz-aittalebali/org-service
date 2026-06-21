package org.dxc.orgservice.company.features.queries.get_company_by_id.application;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.in.IGetCompanyByIdHandler;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.out.IGetCompanyByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetCompanyByIdHandler implements IGetCompanyByIdHandler {

    private final IGetCompanyByIdReadModel readModel;

    public GetCompanyByIdHandler(IGetCompanyByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public CompanyResponse handle(GetCompanyByIdQuery query) {
        return readModel.findById(query.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", query.companyId()));
    }
}
