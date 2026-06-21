package org.dxc.orgservice.country.features.queries.filter_countries.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.filter_countries.application.FilterCountriesQuery;
import org.dxc.orgservice.country.features.queries.filter_countries.application.port.in.IFilterCountriesHandler;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.shared.query.pagination.PageResult;
import org.dxc.orgservice.shared.query.pagination.SortQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Countries", description = "Country management")
@RestController
@RequestMapping("/api/v1/countries")
public class FilterCountriesController {

    private final IFilterCountriesHandler handler;

    public FilterCountriesController(IFilterCountriesHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List countries", description = "Returns a paginated list of countries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Countries retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PageResult<CountryResponse>> filter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(handler.handle(
                new FilterCountriesQuery(new PageQuery(page, size), new SortQuery(sortField, sortDirection))));
    }
}
