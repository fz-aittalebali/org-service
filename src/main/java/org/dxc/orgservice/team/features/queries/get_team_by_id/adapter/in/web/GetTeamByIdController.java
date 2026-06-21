package org.dxc.orgservice.team.features.queries.get_team_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.GetTeamByIdQuery;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.in.IGetTeamByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class GetTeamByIdController {

    private final IGetTeamByIdHandler handler;

    public GetTeamByIdController(IGetTeamByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a team by ID", description = "Returns a single team by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team found"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetTeamByIdQuery(id)));
    }
}
