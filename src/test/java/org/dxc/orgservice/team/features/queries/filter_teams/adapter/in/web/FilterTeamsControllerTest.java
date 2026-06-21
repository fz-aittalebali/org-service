package org.dxc.orgservice.team.features.queries.filter_teams.adapter.in.web;

import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.queries.filter_teams.application.port.in.IFilterTeamsHandler;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilterTeamsController.class)
@DisplayName("GET /api/v1/teams")
class FilterTeamsControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IFilterTeamsHandler handler;

    private static final UUID DEPARTMENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Nested
    @DisplayName("200 OK — with results")
    class WithResults {

        @Test
        @DisplayName("should return 200 with paginated team list")
        void should_return_200_with_list() throws Exception {
            UUID teamId = UUID.randomUUID();
            TeamResponse response = new TeamResponse(teamId, "Backend", DEPARTMENT_ID, Instant.parse("2024-01-01T00:00:00Z"));
            Page<TeamResponse> page = new PageImpl<>(List.of(response));
            when(handler.handle(any())).thenReturn(page);

            mockMvc.perform(get("/api/v1/teams")
                            .with(adminJwt())
                            .param("departmentId", DEPARTMENT_ID.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Backend"))
                    .andExpect(jsonPath("$.content[0].departmentId").value(DEPARTMENT_ID.toString()));
        }
    }

    @Nested
    @DisplayName("200 OK — empty results")
    class Empty {

        @Test
        @DisplayName("should return 200 with empty content when no teams match")
        void should_return_200_empty() throws Exception {
            when(handler.handle(any())).thenReturn(Page.empty());

            mockMvc.perform(get("/api/v1/teams").with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(get("/api/v1/teams"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
