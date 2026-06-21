package org.dxc.orgservice.country.features.commands.delete_country.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.country.features.commands.delete_country.application.DeleteCountryCommand;
import org.dxc.orgservice.country.features.commands.delete_country.application.port.in.IDeleteCountryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Countries", description = "Country management")
@RestController
@RequestMapping("/api/v1/countries")
public class DeleteCountryController {

    private final IDeleteCountryHandler handler;

    public DeleteCountryController(IDeleteCountryHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a country", description = "Deletes a country by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteCountryCommand(id));
        return ResponseEntity.noContent().build();
    }
}
