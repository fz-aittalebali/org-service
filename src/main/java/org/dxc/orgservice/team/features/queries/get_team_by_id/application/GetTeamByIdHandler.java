package org.dxc.orgservice.team.features.queries.get_team_by_id.application;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.in.IGetTeamByIdHandler;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.out.IGetTeamByIdReadModel;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;

public class GetTeamByIdHandler implements IGetTeamByIdHandler {

    private final IGetTeamByIdReadModel readModel;

    public GetTeamByIdHandler(IGetTeamByIdReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public TeamResponse handle(GetTeamByIdQuery query) {
        return readModel.findById(query.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", query.teamId()));
    }
}
