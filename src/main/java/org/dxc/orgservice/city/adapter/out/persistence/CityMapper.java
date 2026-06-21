package org.dxc.orgservice.city.adapter.out.persistence;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(city.getName().value())")
    @Mapping(target = "zipCode", expression = "java(city.getZipCode().value())")
    @Mapping(target = "countryId", expression = "java(city.getCountryId().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    CityJpaEntity toJpaEntity(City city);

    default City toDomain(CityJpaEntity entity) {
        return City.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                ZipCode.of(entity.getZipCode()),
                CountryId.of(entity.getCountryId()),
                entity.getDomainCreatedAt()
        );
    }
}
