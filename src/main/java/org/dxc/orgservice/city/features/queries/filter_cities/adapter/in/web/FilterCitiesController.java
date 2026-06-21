package org.dxc.orgservice.city.features.queries.filter_cities.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.filter_cities.application.FilterCitiesQuery;
import org.dxc.orgservice.city.features.queries.filter_cities.application.port.in.IFilterCitiesHandler;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/v1/cities")
public class FilterCitiesController {

    private final IFilterCitiesHandler handler;

    public FilterCitiesController(IFilterCitiesHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List cities", description = "Returns a paginated list of cities, optionally filtered by country.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cities retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CityResponse>> filter(
            @RequestParam Optional<UUID> countryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(handler.handle(new FilterCitiesQuery(countryId, page, size)));
    }
}
