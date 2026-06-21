package org.dxc.orgservice.city.features.commands.create_city.adapter.in.web;

import org.dxc.orgservice.city.features.commands.create_city.application.port.in.ICreateCityHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreateCityController.class)
@DisplayName("POST /api/v1/cities")
class CreateCityControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateCityHandler handler;

    private static final UUID CREATED_ID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(
                new CreateCityRequest("Casablanca", "20000", UUID.randomUUID()));
    }

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201 with Location header and id in body")
        void should_return_201() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/cities")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/v1/cities/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));
        }
    }

    @Nested
    @DisplayName("400 Bad Request — Bean Validation")
    class ValidationErrors {

        @Test
        @DisplayName("name blank → 400")
        void should_return_400_when_name_blank() throws Exception {
            mockMvc.perform(post("/api/v1/cities")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new CreateCityRequest("", "20000", UUID.randomUUID()))))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(handler);
        }

        @Test
        @DisplayName("countryId null → 400")
        void should_return_400_when_country_id_null() throws Exception {
            String body = """
                    {"name":"Casablanca","zipCode":"20000","countryId":null}
                    """;
            mockMvc.perform(post("/api/v1/cities")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(handler);
        }
    }

    @Nested
    @DisplayName("404 / 409 business errors")
    class BusinessErrors {

        @Test
        @DisplayName("country not found → 404")
        void should_return_404_when_country_not_found() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new ResourceNotFoundException("Country", UUID.randomUUID()));

            mockMvc.perform(post("/api/v1/cities")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("duplicate city name → 409")
        void should_return_409_when_duplicate() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new DuplicateResourceException("City", "Casablanca"));

            mockMvc.perform(post("/api/v1/cities")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(post("/api/v1/cities")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(post("/api/v1/cities")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
