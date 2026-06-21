package org.dxc.orgservice.company.features.commands.create_company.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.company.features.commands.create_company.application.CreateCompanyCommand;
import org.dxc.orgservice.company.features.commands.create_company.application.port.in.ICreateCompanyHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Companies", description = "Company management")
@RestController
@RequestMapping("/api/v1/companies")
public class CreateCompanyController {

    private final ICreateCompanyHandler handler;

    public CreateCompanyController(ICreateCompanyHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a company", description = "Creates a new company. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Company created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Company with this name already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateCompanyRequest request) {
        UUID id = handler.handle(new CreateCompanyCommand(OrgName.of(request.name())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
