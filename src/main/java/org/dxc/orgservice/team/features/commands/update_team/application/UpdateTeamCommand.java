package org.dxc.orgservice.team.features.commands.update_team.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateTeamCommand(UUID teamId, OrgName name) {}
