package org.dxc.orgservice.team.features.queries.get_team_hierarchy.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.GetTeamHierarchyQuery;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.in.IGetTeamHierarchyHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class GetTeamHierarchyController {

    private final IGetTeamHierarchyHandler handler;

    public GetTeamHierarchyController(IGetTeamHierarchyHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get team hierarchy", description = "Returns the full organizational hierarchy for a team: team → department → campus → company → city → country.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team hierarchy found"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<TeamHierarchyResponse> getHierarchy(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetTeamHierarchyQuery(id)));
    }
}
