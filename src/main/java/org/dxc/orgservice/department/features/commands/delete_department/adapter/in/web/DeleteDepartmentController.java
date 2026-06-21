package org.dxc.orgservice.department.features.commands.delete_department.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.dxc.orgservice.department.features.commands.delete_department.application.DeleteDepartmentCommand;
import org.dxc.orgservice.department.features.commands.delete_department.application.port.in.IDeleteDepartmentHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Departments", description = "Department management")
@RestController
@RequestMapping("/api/v1/departments")
public class DeleteDepartmentController {

    private final IDeleteDepartmentHandler handler;

    public DeleteDepartmentController(IDeleteDepartmentHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Delete a department", description = "Deletes a department by its ID. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        handler.handle(new DeleteDepartmentCommand(id));
        return ResponseEntity.noContent().build();
    }
}
