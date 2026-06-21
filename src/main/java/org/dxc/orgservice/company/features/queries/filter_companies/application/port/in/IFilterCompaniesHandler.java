package org.dxc.orgservice.company.features.queries.filter_companies.application.port.in;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.filter_companies.application.FilterCompaniesQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.springframework.data.domain.Page;

public interface IFilterCompaniesHandler extends IQueryHandler<FilterCompaniesQuery, Page<CompanyResponse>> {}
