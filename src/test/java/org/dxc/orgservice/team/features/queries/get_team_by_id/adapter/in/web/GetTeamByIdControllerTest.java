package org.dxc.orgservice.team.features.queries.get_team_by_id.adapter.in.web;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.in.IGetTeamByIdHandler;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GetTeamByIdController.class)
@DisplayName("GET /api/v1/teams/{id}")
class GetTeamByIdControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IGetTeamByIdHandler handler;

    private static final UUID TEAM_ID       = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID DEPARTMENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Nested
    @DisplayName("200 OK")
    class Found {

        @Test
        @DisplayName("should return 200 with team data")
        void should_return_200() throws Exception {
            TeamResponse response = new TeamResponse(TEAM_ID, "Backend", DEPARTMENT_ID, Instant.parse("2024-01-01T00:00:00Z"));
            when(handler.handle(any())).thenReturn(response);

            mockMvc.perform(get("/api/v1/teams/{id}", TEAM_ID).with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(TEAM_ID.toString()))
                    .andExpect(jsonPath("$.name").value("Backend"))
                    .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID.toString()));
        }
    }

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404() throws Exception {
            when(handler.handle(any())).thenThrow(new ResourceNotFoundException("Team", TEAM_ID));
            mockMvc.perform(get("/api/v1/teams/{id}", TEAM_ID).with(adminJwt()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(get("/api/v1/teams/{id}", TEAM_ID))
                    .andExpect(status().isUnauthorized());
        }
    }
}
