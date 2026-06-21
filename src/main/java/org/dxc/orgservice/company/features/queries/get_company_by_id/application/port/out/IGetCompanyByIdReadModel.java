package org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.out;

import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetCompanyByIdReadModel {
    Optional<CompanyResponse> findById(UUID id);
}
