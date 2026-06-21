package org.dxc.orgservice.country.features.commands.create_country.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.country.features.commands.create_country.application.CreateCountryCommand;
import org.dxc.orgservice.country.features.commands.create_country.application.port.in.ICreateCountryHandler;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Countries", description = "Country management")
@RestController
@RequestMapping("/api/v1/countries")
public class CreateCountryController {

    private final ICreateCountryHandler handler;

    public CreateCountryController(ICreateCountryHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a country", description = "Creates a new country. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Country created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Country with this name already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateCountryRequest request) {
        UUID id = handler.handle(new CreateCountryCommand(OrgName.of(request.name())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
