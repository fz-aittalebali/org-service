package org.dxc.orgservice.team.features.commands.update_team.adapter.in.web;

import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.commands.update_team.application.port.in.IUpdateTeamHandler;
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

@WebMvcTest(UpdateTeamController.class)
@DisplayName("PUT /api/v1/teams/{id}")
class UpdateTeamControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IUpdateTeamHandler handler;

    private static final UUID TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new UpdateTeamRequest("Frontend"));
    }

    @Nested
    @DisplayName("204 No Content")
    class Updated {

        @Test
        @DisplayName("should return 204 on success")
        void should_return_204() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNoContent());

            verify(handler).handle(any());
        }
    }

    @Nested
    @DisplayName("400 Bad Request")
    class BadRequest {

        @Test
        @DisplayName("blank name → 400")
        void should_return_400_blank_name() throws Exception {
            String body = objectMapper.writeValueAsString(new UpdateTeamRequest(""));
            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("name exceeds 150 chars → 400")
        void should_return_400_name_too_long() throws Exception {
            String body = objectMapper.writeValueAsString(new UpdateTeamRequest("A".repeat(151)));
            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Business errors")
    class BusinessErrors {

        @Test
        @DisplayName("team not found → 404")
        void should_return_404() throws Exception {
            doThrow(new ResourceNotFoundException("Team", TEAM_ID)).when(handler).handle(any());

            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("duplicate team name → 409")
        void should_return_409() throws Exception {
            doThrow(new DuplicateResourceException("Team", "Frontend")).when(handler).handle(any());

            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
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
            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(put("/api/v1/teams/{id}", TEAM_ID)
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
