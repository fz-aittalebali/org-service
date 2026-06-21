package org.dxc.orgservice.department.features.commands.create_department.application;

import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

public record CreateDepartmentCommand(OrgName name, CampusId campusId) {}
