package org.dxc.orgservice.city.features.commands.update_city.application;

import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateCityCommand(UUID cityId, OrgName newName, ZipCode newZipCode) {}
