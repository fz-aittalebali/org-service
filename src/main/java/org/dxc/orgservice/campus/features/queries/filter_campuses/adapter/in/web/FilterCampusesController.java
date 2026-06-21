package org.dxc.orgservice.campus.features.queries.filter_campuses.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.FilterCampusesQuery;
import org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.in.IFilterCampusesHandler;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Campuses", description = "Campus management")
@RestController
@RequestMapping("/api/v1/campuses")
public class FilterCampusesController {

    private final IFilterCampusesHandler handler;

    public FilterCampusesController(IFilterCampusesHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List campuses", description = "Returns a paginated list of campuses, optionally filtered by company.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campuses retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CampusResponse>> filter(
            @RequestParam Optional<UUID> companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(handler.handle(new FilterCampusesQuery(companyId, page, size)));
    }
}
