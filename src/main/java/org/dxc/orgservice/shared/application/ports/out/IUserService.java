package org.dxc.orgservice.shared.application.ports.out;

import org.dxc.orgservice.shared.query.dtos.UserPageResponse;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserPageResponse getUsersByTeamIds(List<UUID> teamIds, String role, int page, int size);
}
