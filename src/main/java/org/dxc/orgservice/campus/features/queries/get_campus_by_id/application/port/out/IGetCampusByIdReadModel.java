package org.dxc.orgservice.campus.features.queries.get_campus_by_id.application.port.out;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;

import java.util.Optional;
import java.util.UUID;

public interface IGetCampusByIdReadModel {
    Optional<CampusResponse> findById(UUID id);
}
