package org.dxc.orgservice.campus.features.queries.filter_campuses.application.port.out;

import org.dxc.orgservice.campus.features.queries.shared.dto.CampusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IFilterCampusesReadModel {
    Page<CampusResponse> findAll(Optional<UUID> companyId, Pageable pageable);
}
