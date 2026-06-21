package org.dxc.orgservice.company.features.queries.filter_companies.application.port.out;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFilterCompaniesReadModel {
    Page<CompanyResponse> findAll(Pageable pageable);
}
