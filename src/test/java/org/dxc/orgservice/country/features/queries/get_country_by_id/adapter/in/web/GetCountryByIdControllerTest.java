package org.dxc.orgservice.country.features.queries.get_country_by_id.adapter.in.web;

import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.in.IGetCountryByIdHandler;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GetCountryByIdController.class)
@DisplayName("GET /api/v1/countries/{id}")
class GetCountryByIdControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IGetCountryByIdHandler handler;

    private static final UUID COUNTRY_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    // =========================================================================
    // 200 OK
    // =========================================================================

    @Nested
    @DisplayName("200 OK")
    class Found {

        @Test
        @DisplayName("should return 200 with country data in the body")
        void should_return_200_with_country_data() throws Exception {
            CountryResponse response = new CountryResponse(COUNTRY_ID, "Morocco", Instant.now());
            when(handler.handle(any())).thenReturn(response);

            mockMvc.perform(get("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(COUNTRY_ID.toString())))
                    .andExpect(jsonPath("$.name", is("Morocco")));
        }
    }

    // =========================================================================
    // 404 Not Found
    // =========================================================================

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404_when_not_found() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new ResourceNotFoundException("Country", COUNTRY_ID));

            mockMvc.perform(get("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt()))
                    .andExpect(status().isNotFound());
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
            mockMvc.perform(get("/api/v1/countries/{id}", COUNTRY_ID))
                    .andExpect(status().isUnauthorized());
        }
    }
}
