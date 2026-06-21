package org.dxc.orgservice.team.features.queries.get_team_hierarchy.application;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.in.IGetTeamHierarchyHandler;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.out.IGetTeamHierarchyReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetTeamHierarchyHandler implements IGetTeamHierarchyHandler {

    private final IGetTeamHierarchyReadModel readModel;

    public GetTeamHierarchyHandler(IGetTeamHierarchyReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public TeamHierarchyResponse handle(GetTeamHierarchyQuery query) {
        return readModel.findHierarchyByTeamId(query.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", query.teamId()));
    }
}
