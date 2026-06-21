package org.dxc.orgservice.city.features.commands.update_city.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCityRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 20) String zipCode
) {}
