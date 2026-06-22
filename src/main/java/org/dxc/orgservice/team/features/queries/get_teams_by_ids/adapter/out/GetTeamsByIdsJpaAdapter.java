package org.dxc.orgservice.team.features.queries.get_teams_by_ids.adapter.out;

import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;
import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.out.IGetTeamsByIdsReadModel;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetTeamsByIdsJpaAdapter implements IGetTeamsByIdsReadModel {

    private final ITeamJpaRepository jpaRepository;

    public GetTeamsByIdsJpaAdapter(ITeamJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<TeamResponse> findByIds(List<UUID> ids) {
        return jpaRepository.findAllById(ids).stream()
                .map(e -> new TeamResponse(e.getId(), e.getName(),
                        e.getDepartmentId(), e.getDomainCreatedAt()))
                .toList();
    }
}
