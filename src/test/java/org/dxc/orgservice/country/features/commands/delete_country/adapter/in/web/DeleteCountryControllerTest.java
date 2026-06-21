package org.dxc.orgservice.country.features.commands.delete_country.adapter.in.web;

import org.dxc.orgservice.country.features.commands.delete_country.application.port.in.IDeleteCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteCountryController.class)
@DisplayName("DELETE /api/v1/countries/{id}")
class DeleteCountryControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IDeleteCountryHandler handler;

    private static final UUID COUNTRY_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    // =========================================================================
    // 204 No Content
    // =========================================================================

    @Nested
    @DisplayName("204 No Content")
    class Deleted {

        @Test
        @DisplayName("should return 204 when the deletion succeeds")
        void should_return_204_on_success() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(delete("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(adminJwt()))
                    .andExpect(status().isNoContent());

            verify(handler).handle(any());
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

            mockMvc.perform(delete("/api/v1/countries/{id}", COUNTRY_ID)
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
            mockMvc.perform(delete("/api/v1/countries/{id}", COUNTRY_ID))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(handler);
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403_when_not_admin() throws Exception {
            mockMvc.perform(delete("/api/v1/countries/{id}", COUNTRY_ID)
                            .with(nonAdminJwt()))
                    .andExpect(status().isForbidden());

            verifyNoInteractions(handler);
        }
    }
}
