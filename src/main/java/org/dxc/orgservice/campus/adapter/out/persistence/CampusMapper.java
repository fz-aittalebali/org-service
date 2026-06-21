package org.dxc.orgservice.campus.adapter.out.persistence;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CampusMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(campus.getName().value())")
    @Mapping(target = "companyId", expression = "java(campus.getCompanyId().value())")
    @Mapping(target = "cityId", expression = "java(campus.getCityId().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    CampusJpaEntity toJpaEntity(Campus campus);

    default Campus toDomain(CampusJpaEntity entity) {
        return Campus.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                CompanyId.of(entity.getCompanyId()),
                CityId.of(entity.getCityId()),
                entity.getDomainCreatedAt()
        );
    }
}
