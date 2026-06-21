package org.dxc.orgservice.company.domain.repository;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.util.Optional;
import java.util.UUID;

public interface ICompanyRepository {
    void save(Company company);
    Optional<Company> findById(UUID id);
    boolean existsByName(OrgName name);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
