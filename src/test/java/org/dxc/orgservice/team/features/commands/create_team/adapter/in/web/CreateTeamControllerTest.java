package org.dxc.orgservice.team.features.commands.create_team.adapter.in.web;

import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.commands.create_team.application.port.in.ICreateTeamHandler;
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

@WebMvcTest(CreateTeamController.class)
@DisplayName("POST /api/v1/teams")
class CreateTeamControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateTeamHandler handler;

    private static final UUID CREATED_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(
                new CreateTeamRequest("Backend", UUID.randomUUID()));
    }

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201 with Location header and id in body")
        void should_return_201() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/teams")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/v1/teams/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));
        }
    }

    @Nested
    @DisplayName("400 Bad Request")
    class BadRequest {

        @Test
        @DisplayName("blank name → 400")
        void should_return_400_blank_name() throws Exception {
            String body = objectMapper.writeValueAsString(
                    new CreateTeamRequest("", UUID.randomUUID()));
            mockMvc.perform(post("/api/v1/teams")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("name exceeds 150 chars → 400")
        void should_return_400_name_too_long() throws Exception {
            String body = objectMapper.writeValueAsString(
                    new CreateTeamRequest("A".repeat(151), UUID.randomUUID()));
            mockMvc.perform(post("/api/v1/teams")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("null departmentId → 400")
        void should_return_400_null_department_id() throws Exception {
            String body = objectMapper.writeValueAsString(
                    new CreateTeamRequest("Backend", null));
            mockMvc.perform(post("/api/v1/teams")
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
        @DisplayName("department not found → 404")
        void should_return_404_when_department_missing() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new ResourceNotFoundException("Department", UUID.randomUUID()));
            mockMvc.perform(post("/api/v1/teams")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("duplicate team name → 409")
        void should_return_409_when_duplicate() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new DuplicateResourceException("Team", "Backend"));
            mockMvc.perform(post("/api/v1/teams")
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
            mockMvc.perform(post("/api/v1/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(post("/api/v1/teams")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
