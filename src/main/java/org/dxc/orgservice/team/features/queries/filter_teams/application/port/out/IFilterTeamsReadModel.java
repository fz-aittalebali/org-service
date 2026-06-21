package org.dxc.orgservice.team.features.queries.filter_teams.application.port.out;

import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IFilterTeamsReadModel {
    Page<TeamResponse> findAll(Optional<UUID> departmentId, Pageable pageable);
}
