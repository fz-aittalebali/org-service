package org.dxc.orgservice.team.features.commands.delete_team.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.team.features.commands.delete_team.application.DeleteTeamCommand;
import org.dxc.orgservice.team.features.commands.delete_team.application.port.in.IDeleteTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class DeleteTeamController {

    private final IDeleteTeamHandler handler;

    public DeleteTeamController(IDeleteTeamHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a team", description = "Deletes a team by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Team deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteTeamCommand(id));
        return ResponseEntity.noContent().build();
    }
}
