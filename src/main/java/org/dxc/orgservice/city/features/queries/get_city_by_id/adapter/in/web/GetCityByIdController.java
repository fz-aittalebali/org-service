package org.dxc.orgservice.city.features.queries.get_city_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.GetCityByIdQuery;
import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.in.IGetCityByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/v1/cities")
public class GetCityByIdController {

    private final IGetCityByIdHandler handler;

    public GetCityByIdController(IGetCityByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a city by ID", description = "Returns a single city by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "City found"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetCityByIdQuery(id)));
    }
}
