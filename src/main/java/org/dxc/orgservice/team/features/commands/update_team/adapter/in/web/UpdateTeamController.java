package org.dxc.orgservice.team.features.commands.update_team.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.features.commands.update_team.application.UpdateTeamCommand;
import org.dxc.orgservice.team.features.commands.update_team.application.port.in.IUpdateTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class UpdateTeamController {

    private final IUpdateTeamHandler handler;

    public UpdateTeamController(IUpdateTeamHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a team", description = "Updates the name of an existing team. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Team updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "409", description = "Team with this name already exists in the department"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateTeamRequest request) {
        handler.handle(new UpdateTeamCommand(id, OrgName.of(request.name())));
        return ResponseEntity.noContent().build();
    }
}
