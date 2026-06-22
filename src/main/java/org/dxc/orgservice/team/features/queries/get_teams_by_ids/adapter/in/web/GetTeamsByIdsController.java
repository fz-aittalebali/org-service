package org.dxc.orgservice.team.features.queries.get_teams_by_ids.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.GetTeamsByIdsQuery;
import org.dxc.orgservice.team.features.queries.get_teams_by_ids.application.port.in.IGetTeamsByIdsHandler;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class GetTeamsByIdsController {

    private final IGetTeamsByIdsHandler handler;

    public GetTeamsByIdsController(IGetTeamsByIdsHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get teams by a list of IDs",
            description = "Returns all teams matching the provided UUIDs. " +
                    "Unknown IDs are silently ignored. Used by user-service for batch enrichment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of matched teams"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID in ids parameter")
    })
    @GetMapping("/by-ids")
    public ResponseEntity<List<TeamResponse>> getByIds(@RequestParam List<UUID> ids) {
        return ResponseEntity.ok(handler.handle(new GetTeamsByIdsQuery(ids)));
    }
}
