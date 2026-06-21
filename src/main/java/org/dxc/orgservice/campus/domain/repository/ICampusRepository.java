package org.dxc.orgservice.campus.domain.repository;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.Optional;
import java.util.UUID;

public interface ICampusRepository {
    void save(Campus campus);
    Optional<Campus> findById(UUID id);
    boolean existsByNameAndCompanyId(OrgName name, CompanyId companyId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
