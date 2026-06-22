package org.dxc.orgservice.shared.adapter.out.client;

import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${feign.clients.user-service.url}")
public interface UserServiceOpenfeignClient {

    @GetMapping("/api/v1/users/by-team-ids")
    UserPageResponse getUsersByTeamIds(
            @RequestParam("teamIds")                   List<UUID> teamIds,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam("page")                      int page,
            @RequestParam("size")                      int size
    );
}
