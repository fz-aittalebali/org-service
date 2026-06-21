package org.dxc.orgservice.company.features.queries.get_company_by_id.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dxc.orgservice.company.features.queries.shared.dto.CompanyResponse;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.GetCompanyByIdQuery;
import org.dxc.orgservice.company.features.queries.get_company_by_id.application.port.in.IGetCompanyByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Companies", description = "Company management")
@RestController
@RequestMapping("/api/v1/companies")
public class GetCompanyByIdController {

    private final IGetCompanyByIdHandler handler;

    public GetCompanyByIdController(IGetCompanyByIdHandler handler) {
        this.handler = handler;
    }

    @Operation(summary = "Get a company by ID", description = "Returns a single company by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company found"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(handler.handle(new GetCompanyByIdQuery(id)));
    }
}
