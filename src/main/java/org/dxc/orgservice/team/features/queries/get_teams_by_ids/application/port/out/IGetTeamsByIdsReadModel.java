package org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.out;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;

import java.util.List;
import java.util.UUID;

public interface IGetTeamsByIdsReadModel {
    List<TeamResponse> findByIds(List<UUID> ids);
}
