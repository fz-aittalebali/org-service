package org.dxc.orgservice.country.features.commands.create_country.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCountryRequest(
        @NotBlank @Size(max = 150) String name
) {}
