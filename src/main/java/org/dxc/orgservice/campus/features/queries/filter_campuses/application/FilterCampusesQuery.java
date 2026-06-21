package org.dxc.orgservice.campus.features.queries.filter_campuses.application;

import java.util.Optional;
import java.util.UUID;

public record FilterCampusesQuery(Optional<UUID> companyId, int page, int size) {}
