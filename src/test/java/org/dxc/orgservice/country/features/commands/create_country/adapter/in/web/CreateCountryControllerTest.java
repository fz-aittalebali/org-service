package org.dxc.orgservice.country.features.commands.create_country.adapter.in.web;

import org.dxc.orgservice.country.features.commands.create_country.application.port.in.ICreateCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
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

@WebMvcTest(CreateCountryController.class)
@DisplayName("POST /api/v1/countries")
class CreateCountryControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateCountryHandler handler;

    private static final UUID CREATED_ID =
            UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new CreateCountryRequest("Morocco"));
    }

    // =========================================================================
    // 201 Created
    // =========================================================================

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201, a Location header, and the id in the body")
        void should_return_201_with_location_and_id() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/countries")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location",
                            containsString("/api/v1/countries/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));

            verify(handler).handle(any());
        }
    }

    // =========================================================================
    // 400 Bad Request — Bean Validation
    // =========================================================================

    @Nested
    @DisplayName("400 Bad Request — Bean Validation")
    class ValidationErrors {

        @Test
        @DisplayName("name blank → 400")
        void should_return_400_when_name_is_blank() throws Exception {
            mockMvc.perform(post("/api/v1/countries")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateCountryRequest(""))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(handler);
        }

        @Test
        @DisplayName("name exceeds 150 chars → 400")
        void should_return_400_when_name_too_long() throws Exception {
            mockMvc.perform(post("/api/v1/countries")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new CreateCountryRequest("A".repeat(151)))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(handler);
        }
    }

    // =========================================================================
    // 409 Conflict
    // =========================================================================

    @Nested
    @DisplayName("409 Conflict — duplicate name")
    class DuplicateName {

        @Test
        @DisplayName("handler throws DuplicateResourceException → 409")
        void should_return_409_when_name_already_exists() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new DuplicateResourceException("Country", "Morocco"));

            mockMvc.perform(post("/api/v1/countries")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isConflict());
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
            mockMvc.perform(post("/api/v1/countries")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(handler);
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403_when_not_admin() throws Exception {
            mockMvc.perform(post("/api/v1/countries")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());

            verifyNoInteractions(handler);
        }
    }
}
