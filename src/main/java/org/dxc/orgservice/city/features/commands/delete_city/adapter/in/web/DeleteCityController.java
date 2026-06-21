package org.dxc.orgservice.city.features.commands.delete_city.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.city.features.commands.delete_city.application.DeleteCityCommand;
import org.dxc.orgservice.city.features.commands.delete_city.application.port.in.IDeleteCityHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/v1/cities")
public class DeleteCityController {

    private final IDeleteCityHandler handler;

    public DeleteCityController(IDeleteCityHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a city", description = "Deletes a city by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "City deleted successfully"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteCityCommand(id));
        return ResponseEntity.noContent().build();
    }
}
