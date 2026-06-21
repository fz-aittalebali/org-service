package org.dxc.orgservice.campus.features.commands.update_campus.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.campus.features.commands.update_campus.application.UpdateCampusCommand;
import org.dxc.orgservice.campus.features.commands.update_campus.application.port.in.IUpdateCampusHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class UpdateCampusController {

    private final IUpdateCampusHandler handler;

    public UpdateCampusController(IUpdateCampusHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a campus", description = "Updates the name of an existing campus. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Campus updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Campus not found"),
            @ApiResponse(responseCode = "409", description = "Campus with this name already exists in the company"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateCampusRequest request) {
        handler.handle(new UpdateCampusCommand(id, OrgName.of(request.name())));
        return ResponseEntity.noContent().build();
    }
}
