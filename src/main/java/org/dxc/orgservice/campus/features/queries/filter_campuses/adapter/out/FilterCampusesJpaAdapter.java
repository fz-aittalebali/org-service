package org.dxc.orgservice.campus.features.queries.filter_campuses.adapter.out;

import org.dxc.orgservice.campus.adapter.out.persistence.ICampusJpaRepository;
import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.out.IFilterCampusesReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class FilterCampusesJpaAdapter implements IFilterCampusesReadModel {

    private final ICampusJpaRepository jpaRepository;

    public FilterCampusesJpaAdapter(ICampusJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<CampusResponse> findAll(Optional<UUID> companyId, Pageable pageable) {
        if (companyId.isPresent()) {
            return jpaRepository.findAllByCompanyId(companyId.get(), pageable)
                    .map(e -> new CampusResponse(e.getId(), e.getName(), e.getCompanyId(), e.getCityId(), e.getDomainCreatedAt()));
        }
        return jpaRepository.findAll(pageable)
                .map(e -> new CampusResponse(e.getId(), e.getName(), e.getCompanyId(), e.getCityId(), e.getDomainCreatedAt()));
    }
}
