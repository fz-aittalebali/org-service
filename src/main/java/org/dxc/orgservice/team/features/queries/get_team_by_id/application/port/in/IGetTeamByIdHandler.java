package org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.in;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.GetTeamByIdQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetTeamByIdHandler extends IQueryHandler<GetTeamByIdQuery, TeamResponse> {}
