package org.dxc.orgservice.team.features.queries.get_team_hierarchy.adapter.in.web;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.in.IGetTeamHierarchyHandler;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GetTeamHierarchyController.class)
@DisplayName("GET /api/v1/teams/{id}/hierarchy")
class GetTeamHierarchyControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IGetTeamHierarchyHandler handler;

    private static final UUID TEAM_ID       = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID DEPARTMENT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID CAMPUS_ID     = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID COMPANY_ID    = UUID.fromString("55555555-5555-5555-5555-555555555555");
    private static final UUID CITY_ID       = UUID.fromString("66666666-6666-6666-6666-666666666666");
    private static final UUID COUNTRY_ID    = UUID.fromString("77777777-7777-7777-7777-777777777777");

    @Nested
    @DisplayName("200 OK")
    class Found {

        @Test
        @DisplayName("should return 200 with full hierarchy")
        void should_return_200() throws Exception {
            TeamHierarchyResponse response = new TeamHierarchyResponse(
                    TEAM_ID, "Backend",
                    DEPARTMENT_ID, "Engineering",
                    CAMPUS_ID, "HQ",
                    COMPANY_ID, "DXC Technology",
                    CITY_ID, "Casablanca",
                    COUNTRY_ID, "Morocco"
            );
            when(handler.handle(any())).thenReturn(response);

            mockMvc.perform(get("/api/v1/teams/{id}/hierarchy", TEAM_ID).with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.teamId").value(TEAM_ID.toString()))
                    .andExpect(jsonPath("$.teamName").value("Backend"))
                    .andExpect(jsonPath("$.departmentName").value("Engineering"))
                    .andExpect(jsonPath("$.campusName").value("HQ"))
                    .andExpect(jsonPath("$.companyName").value("DXC Technology"))
                    .andExpect(jsonPath("$.cityName").value("Casablanca"))
                    .andExpect(jsonPath("$.countryName").value("Morocco"));
        }
    }

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404() throws Exception {
            when(handler.handle(any())).thenThrow(new ResourceNotFoundException("Team", TEAM_ID));
            mockMvc.perform(get("/api/v1/teams/{id}/hierarchy", TEAM_ID).with(adminJwt()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(get("/api/v1/teams/{id}/hierarchy", TEAM_ID))
                    .andExpect(status().isUnauthorized());
        }
    }
}
