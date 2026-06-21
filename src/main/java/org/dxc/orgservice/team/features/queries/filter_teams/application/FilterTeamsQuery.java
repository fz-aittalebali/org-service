package org.dxc.orgservice.team.features.queries.filter_teams.application;

import java.util.Optional;
import java.util.UUID;

public record FilterTeamsQuery(Optional<UUID> departmentId, int page, int size) {}
