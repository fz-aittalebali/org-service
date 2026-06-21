package org.dxc.orgservice.company.features.queries.filter_companies.adapter.out;

import org.dxc.orgservice.company.adapter.out.persistence.ICompanyJpaRepository;
import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.filter_companies.application.port.out.IFilterCompaniesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class FilterCompaniesJpaAdapter implements IFilterCompaniesReadModel {

    private final ICompanyJpaRepository jpaRepository;

    public FilterCompaniesJpaAdapter(ICompanyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<CompanyResponse> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(e -> new CompanyResponse(e.getId(), e.getName(), e.getDomainCreatedAt()));
    }
}
