package org.dxc.orgservice.company.adapter.out.persistence;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class CompanyWriteAdapter implements ICompanyRepository {

    private final ICompanyJpaRepository jpaRepository;
    private final CompanyMapper mapper;

    public CompanyWriteAdapter(ICompanyJpaRepository jpaRepository, CompanyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Company company) {
        jpaRepository.save(mapper.toJpaEntity(company));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(OrgName name) {
        return jpaRepository.existsByName(name.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
