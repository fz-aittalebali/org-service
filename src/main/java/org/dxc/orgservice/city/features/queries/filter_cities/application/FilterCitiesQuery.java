package org.dxc.orgservice.city.features.queries.filter_cities.application;

import java.util.Optional;
import java.util.UUID;

public record FilterCitiesQuery(Optional<UUID> countryId, int page, int size) {}
