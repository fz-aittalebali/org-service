package org.dxc.orgservice.campus.features.commands.create_campus.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.campus.features.commands.create_campus.application.CreateCampusCommand;
import org.dxc.orgservice.campus.features.commands.create_campus.application.port.in.ICreateCampusHandler;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class CreateCampusController {

    private final ICreateCampusHandler handler;

    public CreateCampusController(ICreateCampusHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a campus", description = "Creates a new campus under a company and city. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Campus created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Company or city not found"),
            @ApiResponse(responseCode = "409", description = "Campus with this name already exists in the company"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateCampusRequest request) {
        UUID id = handler.handle(new CreateCampusCommand(
                OrgName.of(request.name()),
                CompanyId.of(request.companyId()),
                CityId.of(request.cityId())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
