package org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.in;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.GetTeamHierarchyQuery;
import org.dxc.orgservice.shared.application.ports.in.IQueryHandler;

public interface IGetTeamHierarchyHandler extends IQueryHandler<GetTeamHierarchyQuery, TeamHierarchyResponse> {}
