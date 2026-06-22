package org.dxc.orgservice.team.features.queries.get_teams_by_ids.application;

import java.util.List;
import java.util.UUID;

public record GetTeamsByIdsQuery(List<UUID> ids) {}
