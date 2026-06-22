package org.dxc.orgservice.campus.features.queries.get_users_by_campus.application;

import org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.port.in.IGetUsersByCampusHandler;
import org.dxc.orgservice.department.adapter.out.persistence.IDepartmentJpaRepository;
import org.dxc.orgservice.shared.application.ports.out.IUserService;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;

import java.util.List;
import java.util.UUID;

public class GetUsersByCampusHandler implements IGetUsersByCampusHandler {

    private final IDepartmentJpaRepository departmentRepo;
    private final ITeamJpaRepository       teamRepo;
    private final IUserService             userService;

    public GetUsersByCampusHandler(IDepartmentJpaRepository departmentRepo,
                                   ITeamJpaRepository teamRepo,
                                   IUserService userService) {
        this.departmentRepo = departmentRepo;
        this.teamRepo       = teamRepo;
        this.userService    = userService;
    }

    @Override
    public UserPageResponse handle(GetUsersByCampusQuery query) {
        List<UUID> deptIds = departmentRepo.findIdsByCampusId(query.campusId());
        if (deptIds.isEmpty()) return emptyPage(query);

        List<UUID> teamIds = teamRepo.findIdsByDepartmentIdIn(deptIds);
        if (teamIds.isEmpty()) return emptyPage(query);

        return userService.getUsersByTeamIds(
                teamIds, query.role(),
                query.pageQuery().page(), query.pageQuery().size());
    }

    private UserPageResponse emptyPage(GetUsersByCampusQuery query) {
        return new UserPageResponse(
                List.of(),
                query.pageQuery().page(),
                query.pageQuery().size(),
                0, 0, true);
    }
}
