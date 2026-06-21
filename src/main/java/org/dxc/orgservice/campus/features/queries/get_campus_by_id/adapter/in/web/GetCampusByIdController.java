package org.dxc.orgservice.campus.features.queries.get_campus_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.GetCampusByIdQuery;
import org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.in.IGetCampusByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class GetCampusByIdController {

    private final IGetCampusByIdHandler handler;

    public GetCampusByIdController(IGetCampusByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a campus by ID", description = "Returns a single campus by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus found"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampusResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetCampusByIdQuery(id)));
    }
}
