package org.dxc.orgservice.team.features.queries.get_team_hierarchy.adapter.out;

import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.out.IGetTeamHierarchyReadModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class GetTeamHierarchyJpaAdapter implements IGetTeamHierarchyReadModel {

    private final ITeamJpaRepository jpaRepository;

    public GetTeamHierarchyJpaAdapter(ITeamJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TeamHierarchyResponse> findHierarchyByTeamId(UUID teamId) {
        return jpaRepository.findHierarchyByTeamId(teamId)
                .map(p -> new TeamHierarchyResponse(
                        p.getTeamId(), p.getTeamName(),
                        p.getDepartmentId(), p.getDepartmentName(),
                        p.getCampusId(), p.getCampusName(),
                        p.getCompanyId(), p.getCompanyName(),
                        p.getCityId(), p.getCityName(),
                        p.getCountryId(), p.getCountryName()));
    }
}
