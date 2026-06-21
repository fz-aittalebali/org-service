package org.dxc.orgservice.city.features.queries.get_city_by_id.adapter.in.web;

import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.in.IGetCityByIdHandler;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
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

@WebMvcTest(GetCityByIdController.class)
@DisplayName("GET /api/v1/cities/{id}")
class GetCityByIdControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IGetCityByIdHandler handler;

    private static final UUID CITY_ID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @Nested
    @DisplayName("200 OK")
    class Found {

        @Test
        @DisplayName("should return 200 with city data")
        void should_return_200() throws Exception {
            CityResponse response = new CityResponse(CITY_ID, "Casablanca", "20000", UUID.randomUUID(), Instant.now());
            when(handler.handle(any())).thenReturn(response);

            mockMvc.perform(get("/api/v1/cities/{id}", CITY_ID).with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(CITY_ID.toString())))
                    .andExpect(jsonPath("$.name", is("Casablanca")));
        }
    }

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404() throws Exception {
            when(handler.handle(any())).thenThrow(new ResourceNotFoundException("City", CITY_ID));

            mockMvc.perform(get("/api/v1/cities/{id}", CITY_ID).with(adminJwt()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(get("/api/v1/cities/{id}", CITY_ID))
                    .andExpect(status().isUnauthorized());
        }
    }
}
