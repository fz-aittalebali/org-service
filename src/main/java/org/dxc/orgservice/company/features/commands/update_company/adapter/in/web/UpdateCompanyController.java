package org.dxc.orgservice.company.features.commands.update_company.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.company.features.commands.update_company.application.UpdateCompanyCommand;
import org.dxc.orgservice.company.features.commands.update_company.application.port.in.IUpdateCompanyHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Companies", description = "Company management")
@RestController
@RequestMapping("/api/v1/companies")
public class UpdateCompanyController {

    private final IUpdateCompanyHandler handler;

    public UpdateCompanyController(IUpdateCompanyHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a company", description = "Updates the name of an existing company. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Company updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "409", description = "Company with this name already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateCompanyRequest request) {
        handler.handle(new UpdateCompanyCommand(id, OrgName.of(request.name())));
        return ResponseEntity.noContent().build();
    }
}
