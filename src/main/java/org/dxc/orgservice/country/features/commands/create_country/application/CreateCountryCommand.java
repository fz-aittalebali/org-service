package org.dxc.orgservice.country.features.commands.create_country.application;

import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

public record CreateCountryCommand(OrgName name) {}
