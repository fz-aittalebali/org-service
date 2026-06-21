package org.dxc.orgservice.department.features.commands.update_department.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.department.features.commands.update_department.application.UpdateDepartmentCommand;
import org.dxc.orgservice.department.features.commands.update_department.application.port.in.IUpdateDepartmentHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Departments", description = "Department management")
@RestController
@RequestMapping("/api/v1/departments")
public class UpdateDepartmentController {

    private final IUpdateDepartmentHandler handler;

    public UpdateDepartmentController(IUpdateDepartmentHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Update a department", description = "Updates the name of an existing department. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "409", description = "Department with this name already exists in the campus"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateDepartmentRequest request) {
        handler.handle(new UpdateDepartmentCommand(id, OrgName.of(request.name())));
        return ResponseEntity.noContent().build();
    }
}
