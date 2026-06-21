package org.dxc.orgservice.team.features.commands.create_team.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.features.commands.create_team.application.CreateTeamCommand;
import org.dxc.orgservice.team.features.commands.create_team.application.port.in.ICreateTeamHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Teams", description = "Team management")
@RestController
@RequestMapping("/api/v1/teams")
public class CreateTeamController {

    private final ICreateTeamHandler handler;

    public CreateTeamController(ICreateTeamHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a team", description = "Creates a new team under a department. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Team created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "409", description = "Team with this name already exists in the department"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateTeamRequest request) {
        UUID id = handler.handle(new CreateTeamCommand(
                OrgName.of(request.name()),
                DepartmentId.of(request.departmentId())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
