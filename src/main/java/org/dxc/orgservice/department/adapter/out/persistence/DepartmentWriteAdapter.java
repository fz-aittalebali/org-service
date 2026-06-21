package org.dxc.orgservice.department.adapter.out.persistence;

import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class DepartmentWriteAdapter implements IDepartmentRepository {

    private final IDepartmentJpaRepository jpaRepository;
    private final DepartmentMapper mapper;

    public DepartmentWriteAdapter(IDepartmentJpaRepository jpaRepository, DepartmentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Department department) {
        jpaRepository.save(mapper.toJpaEntity(department));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCampusId(OrgName name, CampusId campusId) {
        return jpaRepository.existsByNameAndCampusId(name.value(), campusId.value());
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
