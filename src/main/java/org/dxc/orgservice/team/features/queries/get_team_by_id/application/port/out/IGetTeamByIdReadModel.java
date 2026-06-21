package org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.out;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetTeamByIdReadModel {
    Optional<TeamResponse> findById(UUID id);
}
