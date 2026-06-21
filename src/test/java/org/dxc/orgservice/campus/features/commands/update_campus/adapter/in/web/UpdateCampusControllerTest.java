package org.dxc.orgservice.campus.features.commands.update_campus.adapter.in.web;

import org.dxc.orgservice.campus.features.commands.update_campus.application.port.in.IUpdateCampusHandler;
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

@WebMvcTest(UpdateCampusController.class)
@DisplayName("PUT /api/v1/campuses/{id}")
class UpdateCampusControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IUpdateCampusHandler handler;

    private static final UUID CAMPUS_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new UpdateCampusRequest("Rabat Campus"));
    }

    @Nested
    @DisplayName("204 No Content")
    class Updated {

        @Test
        @DisplayName("should return 204 on success")
        void should_return_204() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(put("/api/v1/campuses/{id}", CAMPUS_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNoContent());

            verify(handler).handle(any());
        }
    }

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404() throws Exception {
            doThrow(new ResourceNotFoundException("Campus", CAMPUS_ID)).when(handler).handle(any());

            mockMvc.perform(put("/api/v1/campuses/{id}", CAMPUS_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(put("/api/v1/campuses/{id}", CAMPUS_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(put("/api/v1/campuses/{id}", CAMPUS_ID)
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
