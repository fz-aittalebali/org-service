package org.dxc.orgservice.company.features.queries.get_company_by_id.adapter.out;

import org.dxc.orgservice.company.adapter.out.persistence.ICompanyJpaRepository;
import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.out.IGetCompanyByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetCompanyByIdJpaAdapter implements IGetCompanyByIdReadModel {

    private final ICompanyJpaRepository jpaRepository;

    public GetCompanyByIdJpaAdapter(ICompanyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CompanyResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new CompanyResponse(e.getId(), e.getName(), e.getDomainCreatedAt()));
    }
}
