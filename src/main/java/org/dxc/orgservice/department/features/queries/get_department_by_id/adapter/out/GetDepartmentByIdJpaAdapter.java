package org.dxc.orgservice.department.features.queries.get_department_by_id.adapter.out;

import org.dxc.orgservice.department.adapter.out.persistence.IDepartmentJpaRepository;
import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.out.IGetDepartmentByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetDepartmentByIdJpaAdapter implements IGetDepartmentByIdReadModel {

    private final IDepartmentJpaRepository jpaRepository;

    public GetDepartmentByIdJpaAdapter(IDepartmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<DepartmentResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new DepartmentResponse(e.getId(), e.getName(), e.getCampusId(), e.getDomainCreatedAt()));
    }
}
