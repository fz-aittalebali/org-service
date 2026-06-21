package org.dxc.orgservice.team.adapter.out.persistence;

import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.entities.Team;
import org.dxc.orgservice.team.domain.repository.ITeamRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class TeamWriteAdapter implements ITeamRepository {

    private final ITeamJpaRepository jpaRepository;
    private final TeamMapper mapper;

    public TeamWriteAdapter(ITeamJpaRepository jpaRepository, TeamMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Team team) {
        jpaRepository.save(mapper.toJpaEntity(team));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndDepartmentId(OrgName name, DepartmentId departmentId) {
        return jpaRepository.existsByNameAndDepartmentId(name.value(), departmentId.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
