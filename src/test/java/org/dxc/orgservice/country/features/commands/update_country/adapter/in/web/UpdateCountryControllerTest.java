package org.dxc.orgservice.country.features.commands.update_country.adapter.in.web;

import org.dxc.orgservice.country.features.commands.update_country.application.port.in.IUpdateCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateCountryController.class)
@DisplayName("PUT /api/v1/countries/{id}")
class UpdateCountryControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IUpdateCountryHandler handler;

    private static final UUID COUNTRY_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new UpdateCountryRequest("Algeria"));
    }

    // =========================================================================
    // 204 No Content
    // =========================================================================

    @Nested
    @DisplayName("204 No Content")
    class Updated {

        @Test
        @DisplayName("should return 204 when the update succeeds")
        void should_return_204_on_success() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(put("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNoContent());

            verify(handler).handle(any());
        }
    }

    // =========================================================================
    // 400 Bad Request
    // =========================================================================

    @Nested
    @DisplayName("400 Bad Request — Bean Validation")
    class ValidationErrors {

        @Test
        @DisplayName("name blank → 400")
        void should_return_400_when_name_is_blank() throws Exception {
            mockMvc.perform(put("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UpdateCountryRequest(""))))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(handler);
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
        void should_return_404_when_country_not_found() throws Exception {
            doThrow(new ResourceNotFoundException("Country", COUNTRY_ID))
                    .when(handler).handle(any());

            mockMvc.perform(put("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
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
            mockMvc.perform(put("/api/v1/countries/{id}", COUNTRY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(handler);
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403_when_not_admin() throws Exception {
            mockMvc.perform(put("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());

            verifyNoInteractions(handler);
        }
    }
}
