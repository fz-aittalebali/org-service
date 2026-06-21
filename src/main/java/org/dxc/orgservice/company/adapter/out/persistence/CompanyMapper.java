package org.dxc.orgservice.company.adapter.out.persistence;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", expression = "java(company.getName().value())")
    @Mapping(target = "domainCreatedAt", source = "createdAt")
    CompanyJpaEntity toJpaEntity(Company company);

    default Company toDomain(CompanyJpaEntity entity) {
        return Company.reconstitute(
                entity.getId(),
                OrgName.of(entity.getName()),
                entity.getDomainCreatedAt()
        );
    }
}
