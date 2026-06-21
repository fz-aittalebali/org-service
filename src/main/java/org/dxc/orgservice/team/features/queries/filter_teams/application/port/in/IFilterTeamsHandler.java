package org.dxc.orgservice.team.features.queries.filter_teams.application.port.in;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.filter_teams.application.FilterTeamsQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;
import org.springframework.data.domain.Page;

public interface IFilterTeamsHandler extends IQueryHandler<FilterTeamsQuery, Page<TeamResponse>> {}
