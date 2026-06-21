package org.dxc.orgservice.team.features.queries.filter_teams.application;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.filter_teams.application.port.in.IFilterTeamsHandler;
import org.dxc.orgservice.team.features.queries.filter_teams.application.port.out.IFilterTeamsReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class FilterTeamsHandler implements IFilterTeamsHandler {

    private final IFilterTeamsReadModel readModel;

    public FilterTeamsHandler(IFilterTeamsReadModel readModel) {
        this.readModel = readModel;
    }

    @Override
    public Page<TeamResponse> handle(FilterTeamsQuery query) {
        return readModel.findAll(query.departmentId(), PageRequest.of(query.page(), query.size()));
    }
}
