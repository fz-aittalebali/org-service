package org.dxc.orgservice.team.features.commands.create_team.application;

import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

public record CreateTeamCommand(OrgName name, DepartmentId departmentId) {}
