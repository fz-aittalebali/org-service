package org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.in;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.GetCompanyByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetCompanyByIdHandler extends IQueryHandler<GetCompanyByIdQuery, CompanyResponse> {}
