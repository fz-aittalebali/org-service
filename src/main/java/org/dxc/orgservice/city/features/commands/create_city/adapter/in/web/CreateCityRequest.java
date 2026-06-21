package org.dxc.orgservice.city.features.commands.create_city.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCityRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Size(max = 20) String zipCode,
        @NotNull UUID countryId
) {}
