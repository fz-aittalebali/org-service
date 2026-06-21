package org.dxc.orgservice.department.features.queries.get_department_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.GetDepartmentByIdQuery;
import org.dxc.orgservice.department.features.queries.get_department_by_id.application.port.in.IGetDepartmentByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Departments", description = "Department management")
@RestController
@RequestMapping("/api/v1/departments")
public class GetDepartmentByIdController {

    private final IGetDepartmentByIdHandler handler;

    public GetDepartmentByIdController(IGetDepartmentByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a department by ID", description = "Returns a single department by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department found"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetDepartmentByIdQuery(id)));
    }
}
