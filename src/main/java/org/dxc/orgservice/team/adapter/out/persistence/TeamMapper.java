package org.dxc.orgservice.team.adapter.out.persistence;

import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.entities.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(team.getName().value())")
    @Mapping(target = "departmentId", expression = "java(team.getDepartmentId().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    TeamJpaEntity toJpaEntity(Team team);

    default Team toDomain(TeamJpaEntity entity) {
        return Team.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                DepartmentId.of(entity.getDepartmentId()),
                entity.getDomainCreatedAt()
        );
    }
}
