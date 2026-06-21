package org.dxc.orgservice.campus.features.commands.create_campus.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCampusRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull UUID companyId,
        @NotNull UUID cityId
) {}
