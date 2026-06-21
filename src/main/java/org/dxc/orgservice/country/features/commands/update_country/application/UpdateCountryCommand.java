package org.dxc.orgservice.country.features.commands.update_country.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.UUID;

public record UpdateCountryCommand(UUID countryId, OrgName newName) {}
