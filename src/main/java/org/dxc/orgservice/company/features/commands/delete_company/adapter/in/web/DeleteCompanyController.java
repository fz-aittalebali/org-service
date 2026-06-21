package org.dxc.orgservice.company.features.commands.delete_company.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.company.features.commands.delete_company.application.DeleteCompanyCommand;
import org.dxc.orgservice.company.features.commands.delete_company.application.port.in.IDeleteCompanyHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Companies", description = "Company management")
@RestController
@RequestMapping("/api/v1/companies")
public class DeleteCompanyController {

    private final IDeleteCompanyHandler handler;

    public DeleteCompanyController(IDeleteCompanyHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a company", description = "Deletes a company by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteCompanyCommand(id));
        return ResponseEntity.noContent().build();
    }
}
