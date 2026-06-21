package org.dxc.orgservice.department.features.queries.filter_departments.adapter.out;

import org.dxc.orgservice.department.adapter.out.persistence.IDepartmentJpaRepository;
import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.filter_departments.application.port.out.IFilterDepartmentsReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class FilterDepartmentsJpaAdapter implements IFilterDepartmentsReadModel {

    private final IDepartmentJpaRepository jpaRepository;

    public FilterDepartmentsJpaAdapter(IDepartmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<DepartmentResponse> findAll(Optional<UUID> campusId, Pageable pageable) {
        if (campusId.isPresent()) {
            return jpaRepository.findAllByCampusId(campusId.get(), pageable)
                    .map(e -> new DepartmentResponse(e.getId(), e.getName(), e.getCampusId(), e.getDomainCreatedAt()));
        }
        return jpaRepository.findAll(pageable)
                .map(e -> new DepartmentResponse(e.getId(), e.getName(), e.getCampusId(), e.getDomainCreatedAt()));
    }
}
