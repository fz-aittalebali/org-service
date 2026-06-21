package org.dxc.orgservice.department.features.commands.update_department.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDepartmentRequest(
        @NotBlank @Size(max = 150) String name
) {}
