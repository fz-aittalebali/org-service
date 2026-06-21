package org.dxc.orgservice.city.features.commands.create_city.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.city.features.commands.create_city.application.CreateCityCommand;
import org.dxc.orgservice.city.features.commands.create_city.application.port.in.ICreateCityHandler;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/v1/cities")
public class CreateCityController {

    private final ICreateCityHandler handler;

    public CreateCityController(ICreateCityHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a city", description = "Creates a new city within a country. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "City created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Country not found"),
            @ApiResponse(responseCode = "409", description = "City with this name already exists in the country"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateCityRequest request) {
        UUID id = handler.handle(new CreateCityCommand(
                OrgName.of(request.name()),
                ZipCode.of(request.zipCode()),
                CountryId.of(request.countryId())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
