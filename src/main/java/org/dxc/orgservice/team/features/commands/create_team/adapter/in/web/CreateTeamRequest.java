package org.dxc.orgservice.team.features.commands.create_team.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTeamRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull UUID departmentId
) {}
