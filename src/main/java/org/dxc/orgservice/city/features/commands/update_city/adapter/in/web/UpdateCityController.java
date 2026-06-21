package org.dxc.orgservice.city.features.commands.update_city.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.city.features.commands.update_city.application.UpdateCityCommand;
import org.dxc.orgservice.city.features.commands.update_city.application.port.in.IUpdateCityHandler;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/v1/cities")
public class UpdateCityController {

    private final IUpdateCityHandler handler;

    public UpdateCityController(IUpdateCityHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a city", description = "Updates the name and zip code of an existing city. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "City updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "409", description = "City with this name already exists in the country"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateCityRequest request) {
        handler.handle(new UpdateCityCommand(id, OrgName.of(request.name()), ZipCode.of(request.zipCode())));
        return ResponseEntity.noContent().build();
    }
}
