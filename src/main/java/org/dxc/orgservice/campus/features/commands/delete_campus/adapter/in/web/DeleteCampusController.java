package org.dxc.orgservice.campus.features.commands.delete_campus.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.campus.features.commands.delete_campus.application.DeleteCampusCommand;
import org.dxc.orgservice.campus.features.commands.delete_campus.application.port.in.IDeleteCampusHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class DeleteCampusController {

    private final IDeleteCampusHandler handler;

    public DeleteCampusController(IDeleteCampusHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a campus", description = "Deletes a campus by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Campus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Campus not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteCampusCommand(id));
        return ResponseEntity.noContent().build();
    }
}
