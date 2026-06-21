package org.dxc.orgservice.campus.features.commands.update_campus.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateCampusCommand(UUID campusId, OrgName name) {}
