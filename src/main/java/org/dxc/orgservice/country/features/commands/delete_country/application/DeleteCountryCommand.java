package org.dxc.orgservice.country.features.commands.delete_country.application;

import java.util.UUID;

public record DeleteCountryCommand(UUID countryId) {}
