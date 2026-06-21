package org.dxc.orgservice.country.adapter.out.persistence;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(country.getName().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    CountryJpaEntity toJpaEntity(Country country);

    default Country toDomain(CountryJpaEntity entity) {
        return Country.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                entity.getDomainCreatedAt()
        );
    }
}
