package org.dxc.orgservice.country.features.queries.filter_countries.adapter.in.web;

import org.dxc.orgservice.country.features.queries.filter_countries.application.port.in.IFilterCountriesHandler;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.shared.query.pagination.PageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilterCountriesController.class)
@DisplayName("GET /api/v1/countries")
class FilterCountriesControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IFilterCountriesHandler handler;

    // =========================================================================
    // 200 OK
    // =========================================================================

    @Nested
    @DisplayName("200 OK")
    class Listed {

        @Test
        @DisplayName("should return 200 with paginated list of countries")
        void should_return_200_with_list() throws Exception {
            PageResult<CountryResponse> page = new PageResult<>(
                    List.of(new CountryResponse(UUID.randomUUID(), "Morocco", Instant.now())),
                    1, 1, 0, 20
            );
            when(handler.handle(any())).thenReturn(page);

            mockMvc.perform(get("/api/v1/countries")
                            .with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.totalElements", is(1)))
                    .andExpect(jsonPath("$.content[0].name", is("Morocco")));
        }

        @Test
        @DisplayName("should return 200 with empty content when no countries exist")
        void should_return_200_with_empty_content() throws Exception {
            PageResult<CountryResponse> empty = new PageResult<>(List.of(), 0, 0, 0, 20);
            when(handler.handle(any())).thenReturn(empty);

            mockMvc.perform(get("/api/v1/countries")
                            .with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements", is(0)));
        }
    }

    // =========================================================================
    // Security
    // =========================================================================

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no Bearer token → 401")
        void should_return_401_when_no_token() throws Exception {
            mockMvc.perform(get("/api/v1/countries"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
