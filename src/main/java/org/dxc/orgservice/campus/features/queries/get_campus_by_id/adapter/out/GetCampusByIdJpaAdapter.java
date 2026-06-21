package org.dxc.orgservice.campus.features.queries.get_campus_by_id.adapter.out;

import org.dxc.orgservice.campus.adapter.out.persistence.ICampusJpaRepository;
import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.out.IGetCampusByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetCampusByIdJpaAdapter implements IGetCampusByIdReadModel {

    private final ICampusJpaRepository jpaRepository;

    public GetCampusByIdJpaAdapter(ICampusJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CampusResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new CampusResponse(e.getId(), e.getName(), e.getCompanyId(), e.getCityId(), e.getDomainCreatedAt()));
    }
}
