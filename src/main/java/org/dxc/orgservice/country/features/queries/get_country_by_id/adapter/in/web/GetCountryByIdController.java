package org.dxc.orgservice.country.features.queries.get_country_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.GetCountryByIdQuery;
import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.in.IGetCountryByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Countries", description = "Country management")
@RestController
@RequestMapping("/api/v1/countries")
public class GetCountryByIdController {

    private final IGetCountryByIdHandler handler;

    public GetCountryByIdController(IGetCountryByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a country by ID", description = "Returns a single country by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetCountryByIdQuery(id)));
    }
}
