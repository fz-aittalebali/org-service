package org.dxc.orgservice.company.features.queries.filter_companies.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.filter_companies.application.FilterCompaniesQuery;
import org.dxc.orgservice.company.features.queries.filter_companies.application.port.in.IFilterCompaniesHandler;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Companies", description = "Company management")
@RestController
@RequestMapping("/api/v1/companies")
public class FilterCompaniesController {

    private final IFilterCompaniesHandler handler;

    public FilterCompaniesController(IFilterCompaniesHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "List companies", description = "Returns a paginated list of companies.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> filter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(handler.handle(new FilterCompaniesQuery(page, size)));
    }
}
