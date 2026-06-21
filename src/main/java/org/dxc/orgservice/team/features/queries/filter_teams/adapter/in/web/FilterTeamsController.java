package org.dxc.orgservice.team.features.queries.filter_teams.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.filter_teams.application.FilterTeamsQuery;
import org.dxc.orgservice.team.features.queries.filter_teams.application.port.in.IFilterTeamsHandler;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class FilterTeamsController {

    private final IFilterTeamsHandler handler;

    public FilterTeamsController(IFilterTeamsHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List teams", description = "Returns a paginated list of teams, optionally filtered by department.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teams retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<TeamResponse>> filter(
            @RequestParam Optional<UUID> departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(handler.handle(new FilterTeamsQuery(departmentId, page, size)));
    }
}
