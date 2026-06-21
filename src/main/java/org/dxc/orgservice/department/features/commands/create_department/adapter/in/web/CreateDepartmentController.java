package org.dxc.orgservice.department.features.commands.create_department.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.department.features.commands.create_department.application.CreateDepartmentCommand;
import org.dxc.orgservice.department.features.commands.create_department.application.port.in.ICreateDepartmentHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Departments", description = "Department management")
@RestController
@RequestMapping("/api/v1/departments")
public class CreateDepartmentController {

    private final ICreateDepartmentHandler handler;

    public CreateDepartmentController(ICreateDepartmentHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Create a department", description = "Creates a new department under a campus. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Department created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Campus not found"),
            @ApiResponse(responseCode = "409", description = "Department with this name already exists in the campus"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<ResponseBody> create(@Valid @RequestBody CreateDepartmentRequest request) {
        UUID id = handler.handle(new CreateDepartmentCommand(
                OrgName.of(request.name()),
                CampusId.of(request.campusId())));
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
                .body(new ResponseBody(id.toString()));
    }

    public record ResponseBody(String id) {}
}
