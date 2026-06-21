package org.dxc.orgservice.department.features.commands.create_department.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDepartmentRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull UUID campusId
) {}
