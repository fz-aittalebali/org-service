package org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.in;

import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.GetTeamsByIdsQuery;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;

import java.util.List;

public interface IGetTeamsByIdsHandler extends IQueryHandler<GetTeamsByIdsQuery, List<TeamResponse>> {}
