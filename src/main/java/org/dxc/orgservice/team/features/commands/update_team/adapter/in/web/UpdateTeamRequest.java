package org.dxc.orgservice.team.features.commands.update_team.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTeamRequest(
        @NotBlank @Size(max = 150) String name
) {}
