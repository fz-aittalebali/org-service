package org.dxc.orgservice.country.features.queries.filter_countries.application;

import org.dxc.orgservice.country.features.queries.filter_countries.application.port.out.IFilterCountriesReadModel;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.shared.query.pagination.PageResult;
import org.dxc.orgservice.shared.query.pagination.SortQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FilterCountriesHandler")
class FilterCountriesHandlerTest {

    private IFilterCountriesReadModel readModel;
    private FilterCountriesHandler    sut;

    private static final PageQuery PAGE = new PageQuery(0, 20);
    private static final SortQuery SORT = new SortQuery("name", "ASC");

    private static final PageResult<CountryResponse> RESULT = new PageResult<>(
            List.of(new CountryResponse(UUID.randomUUID(), "Morocco", Instant.now())),
            1, 1, 0, 20
    );

    @BeforeEach
    void setUp() {
        readModel = mock(IFilterCountriesReadModel.class);
        sut = new FilterCountriesHandler(readModel);
    }

    @Test
    @DisplayName("should delegate to the read model and return its result")
    void should_delegate_and_return_result() {
        when(readModel.findAll(PAGE, SORT)).thenReturn(RESULT);

        PageResult<CountryResponse> result = sut.handle(new FilterCountriesQuery(PAGE, SORT));

        assertThat(result).isSameAs(RESULT);
    }

    @Test
    @DisplayName("should return empty page when the read model returns no results")
    void should_return_empty_page_when_no_results() {
        PageResult<CountryResponse> empty = new PageResult<>(List.of(), 0, 0, 0, 20);
        when(readModel.findAll(PAGE, SORT)).thenReturn(empty);

        PageResult<CountryResponse> result = sut.handle(new FilterCountriesQuery(PAGE, SORT));

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
