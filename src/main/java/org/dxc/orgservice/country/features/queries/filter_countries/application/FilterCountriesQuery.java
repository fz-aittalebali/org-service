package org.dxc.orgservice.country.features.queries.filter_countries.application;

import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.shared.query.pagination.SortQuery;

public record FilterCountriesQuery(PageQuery page, SortQuery sort) {}
