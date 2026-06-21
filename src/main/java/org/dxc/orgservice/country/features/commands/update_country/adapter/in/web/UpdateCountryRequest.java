package org.dxc.orgservice.country.features.commands.update_country.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCountryRequest(
        @NotBlank @Size(max = 150) String name
) {}
