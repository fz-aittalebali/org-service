package org.dxc.orgservice.campus.adapter.out.persistence;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class CampusWriteAdapter implements ICampusRepository {

    private final ICampusJpaRepository jpaRepository;
    private final CampusMapper mapper;

    public CampusWriteAdapter(ICampusJpaRepository jpaRepository, CampusMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Campus campus) {
        jpaRepository.save(mapper.toJpaEntity(campus));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Campus> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCompanyId(OrgName name, CompanyId companyId) {
        return jpaRepository.existsByNameAndCompanyId(name.value(), companyId.value());
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
