package org.dxc.orgservice.department.adapter.out.persistence;

import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(department.getName().value())")
    @Mapping(target = "campusId", expression = "java(department.getCampusId().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    DepartmentJpaEntity toJpaEntity(Department department);

    default Department toDomain(DepartmentJpaEntity entity) {
        return Department.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                CampusId.of(entity.getCampusId()),
                entity.getDomainCreatedAt()
        );
    }
}
