package org.dxc.orgservice.company.features.commands.update_company.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(
        @NotBlank @Size(max = 150) String name
) {}
