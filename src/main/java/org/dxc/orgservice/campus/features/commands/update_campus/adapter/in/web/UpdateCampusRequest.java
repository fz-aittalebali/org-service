package org.dxc.orgservice.campus.features.commands.update_campus.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCampusRequest(
        @NotBlank @Size(max = 150) String name
) {}
