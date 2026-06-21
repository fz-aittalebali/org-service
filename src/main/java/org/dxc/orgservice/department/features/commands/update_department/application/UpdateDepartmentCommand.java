package org.dxc.orgservice.department.features.commands.update_department.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateDepartmentCommand(UUID departmentId, OrgName name) {}
