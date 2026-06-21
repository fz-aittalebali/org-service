package org.dxc.orgservice.team.features.queries.get_team_by_id.adapter.out;

import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.out.IGetTeamByIdReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetTeamByIdJpaAdapter implements IGetTeamByIdReadModel {

    private final ITeamJpaRepository jpaRepository;

    public GetTeamByIdJpaAdapter(ITeamJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TeamResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(e -> new TeamResponse(e.getId(), e.getName(), e.getDepartmentId(), e.getDomainCreatedAt()));
    }
}
