package org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.out;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetTeamHierarchyReadModel {
    Optional<TeamHierarchyResponse> findHierarchyByTeamId(UUID teamId);
}
