package org.dxc.orgservice.country.features.commands.update_country.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.country.features.commands.update_country.application.UpdateCountryCommand;
import org.dxc.orgservice.country.features.commands.update_country.application.port.in.IUpdateCountryHandler;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Countries", description = "Country management")
@RestController
@RequestMapping("/api/v1/countries")
public class UpdateCountryController {

    private final IUpdateCountryHandler handler;

    public UpdateCountryController(IUpdateCountryHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a country", description = "Updates the name of an existing country. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Country not found"),
            @ApiResponse(responseCode = "409", description = "Country with this name already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateCountryRequest request) {
        handler.handle(new UpdateCountryCommand(id, OrgName.of(request.name())));
        return ResponseEntity.noContent().build();
    }
}
