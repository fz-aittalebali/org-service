package org.dxc.orgservice.team.features.queries.filter_teams.adapter.out;

import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.filter_teams.application.port.out.IFilterTeamsReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class FilterTeamsJpaAdapter implements IFilterTeamsReadModel {

    private final ITeamJpaRepository jpaRepository;

    public FilterTeamsJpaAdapter(ITeamJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<TeamResponse> findAll(Optional<UUID> departmentId, Pageable pageable) {
        if (departmentId.isPresent()) {
            return jpaRepository.findAllByDepartmentId(departmentId.get(), pageable)
                    .map(e -> new TeamResponse(e.getId(), e.getName(), e.getDepartmentId(), e.getDomainCreatedAt()));
        }
        return jpaRepository.findAll(pageable)
                .map(e -> new TeamResponse(e.getId(), e.getName(), e.getDepartmentId(), e.getDomainCreatedAt()));
    }
}
