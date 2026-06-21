package org.dxc.orgservice.city.features.commands.create_city.application;

import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

public record CreateCityCommand(OrgName name, ZipCode zipCode, CountryId countryId) {}
