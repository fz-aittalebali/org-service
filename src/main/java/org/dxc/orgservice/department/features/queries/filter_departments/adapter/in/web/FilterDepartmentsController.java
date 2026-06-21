package org.dxc.orgservice.department.features.queries.filter_departments.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.department.features.queries.shared.dto.DepartmentResponse;
import org.dxc.orgservice.department.features.queries.filter_departments.application.FilterDepartmentsQuery;
import org.dxc.orgservice.department.features.queries.filter_departments.application.port.in.IFilterDepartmentsHandler;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Departments", description = "Department management")
@RestController
@RequestMapping("/api/v1/departments")
public class FilterDepartmentsController {

    private final IFilterDepartmentsHandler handler;

    public FilterDepartmentsController(IFilterDepartmentsHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List departments", description = "Returns a paginated list of departments, optionally filtered by campus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> filter(
            @RequestParam Optional<UUID> campusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(handler.handle(new FilterDepartmentsQuery(campusId, page, size)));
    }
}
