package org.dxc.orgservice.team.domain.repository;

import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.entities.Team;

import java.util.Optional;
import java.util.UUID;

public interface ITeamRepository {
    void save(Team team);
    Optional<Team> findById(UUID id);
    boolean existsByNameAndDepartmentId(OrgName name, DepartmentId departmentId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
