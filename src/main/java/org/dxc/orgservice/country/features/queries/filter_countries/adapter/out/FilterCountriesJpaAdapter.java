package org.dxc.orgservice.country.features.queries.filter_countries.adapter.out;

import org.dxc.orgservice.country.adapter.out.persistence.ICountryJpaRepository;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.country.features.queries.filter_countries.application.port.out.IFilterCountriesReadModel;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.shared.query.pagination.PageResult;
import org.dxc.orgservice.shared.query.pagination.SortQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class FilterCountriesJpaAdapter implements IFilterCountriesReadModel {

    private final ICountryJpaRepository jpaRepository;

    public FilterCountriesJpaAdapter(ICountryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PageResult<CountryResponse> findAll(PageQuery page, SortQuery sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort.direction());
        PageRequest pageable = PageRequest.of(page.page(), page.size(), Sort.by(direction, sort.field()));
        Page<CountryResponse> result = jpaRepository.findAll(pageable)
                .map(e -> new CountryResponse(e.getId(), e.getName(), e.getDomainCreatedAt()));
        return new PageResult<>(result.getContent(), result.getTotalElements(),
                result.getTotalPages(), page.page(), page.size());
    }
}
