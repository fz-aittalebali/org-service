package org.dxc.orgservice.team.features.commands.delete_team.adapter.in.web;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.commands.delete_team.application.port.in.IDeleteTeamHandler;
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

@WebMvcTest(DeleteTeamController.class)
@DisplayName("DELETE /api/v1/teams/{id}")
class DeleteTeamControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IDeleteTeamHandler handler;

    private static final UUID TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Nested
    @DisplayName("204 No Content")
    class Deleted {

        @Test
        @DisplayName("should return 204 on success")
        void should_return_204() throws Exception {
            doNothing().when(handler).handle(any());
            mockMvc.perform(delete("/api/v1/teams/{id}", TEAM_ID).with(adminJwt()))
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
            doThrow(new ResourceNotFoundException("Team", TEAM_ID)).when(handler).handle(any());
            mockMvc.perform(delete("/api/v1/teams/{id}", TEAM_ID).with(adminJwt()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(delete("/api/v1/teams/{id}", TEAM_ID))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(delete("/api/v1/teams/{id}", TEAM_ID).with(nonAdminJwt()))
                    .andExpect(status().isForbidden());
        }
    }
}
