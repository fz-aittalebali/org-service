package org.dxc.orgservice.campus.features.queries.get_users_by_campus.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.GetUsersByCampusQuery;
import org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.port.in.IGetUsersByCampusHandler;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class GetUsersByCampusController {

    private final IGetUsersByCampusHandler handler;

    public GetUsersByCampusController(IGetUsersByCampusHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get users belonging to a campus",
            description = "Resolves campus → departments → teams, then fetches all users " +
                    "in those teams from user-service. Optional role filter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of users returned"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    @GetMapping("/{campusId}/users")
    public ResponseEntity<UserPageResponse> getUsersByCampus(
            @PathVariable UUID campusId,
            @Parameter(description = "Filter by user role",
                    schema = @Schema(allowableValues = {"ADMIN", "CHANGE_LEAD", "TEAM_LEAD",
                            "SUPPLIER_MANAGER", "CHANGE_MANAGER", "INCIDENT_MANAGER"}))
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(handler.handle(
                new GetUsersByCampusQuery(campusId, role, new PageQuery(page, size))));
    }
}
