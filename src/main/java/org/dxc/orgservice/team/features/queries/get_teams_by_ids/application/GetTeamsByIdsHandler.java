package org.dxc.orgservice.team.features.queries.get_teams_by_ids.application;

import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.in.IGetTeamsByIdsHandler;
import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.out.IGetTeamsByIdsReadModel;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;

import java.util.List;

public class GetTeamsByIdsHandler implements IGetTeamsByIdsHandler {

    private final IGetTeamsByIdsReadModel readModel;

    public GetTeamsByIdsHandler(IGetTeamsByIdsReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public List<TeamResponse> handle(GetTeamsByIdsQuery query) {
        return readModel.findByIds(query.ids());
    }
}
