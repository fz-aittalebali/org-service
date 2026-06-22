package org.dxc.orgservice.campus.features.queries.get_users_by_campus.adapter.in.web;

import org.dxc.orgservice.campus.features.queries.get_users_by_campus.application.port.in.IGetUsersByCampusHandler;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.dxc.orgservice.shared.query.dtos.UserSummaryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetUsersByCampusController.class)
@DisplayName("GET /api/v1/campuses/{campusId}/users")
class GetUsersByCampusControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IGetUsersByCampusHandler handler;

    private static final UUID CAMPUS_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

    private UserPageResponse pageWithOneUser() {
        UserSummaryDto alice = new UserSummaryDto(
                UUID.randomUUID(), "Alice", "Durand",
                "alice@dxc.com", "+33600000001", "ACTIVE",
                Instant.parse("2024-01-01T00:00:00Z"));
        return new UserPageResponse(List.of(alice), 0, 20, 1, 1, true);
    }

    private UserPageResponse emptyPage() {
        return new UserPageResponse(List.of(), 0, 20, 0, 0, true);
    }

    // =========================================================================
    // 200 OK
    // =========================================================================

    @Nested
    @DisplayName("200 OK")
    class Found {

        @Test
        @DisplayName("should return 200 with paginated user list for a valid campusId")
        void should_return_200_with_users() throws Exception {
            when(handler.handle(any())).thenReturn(pageWithOneUser());

            mockMvc.perform(get("/api/v1/campuses/{campusId}/users", CAMPUS_ID)
                            .with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].firstName", is("Alice")))
                    .andExpect(jsonPath("$.totalElements", is(1)))
                    .andExpect(jsonPath("$.page", is(0)));

            verify(handler).handle(any());
        }

        @Test
        @DisplayName("should return 200 with empty content when campus has no users")
        void should_return_200_with_empty_content() throws Exception {
            when(handler.handle(any())).thenReturn(emptyPage());

            mockMvc.perform(get("/api/v1/campuses/{campusId}/users", CAMPUS_ID)
                            .with(adminJwt()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements", is(0)));
        }

        @Test
        @DisplayName("optional role filter is accepted — returns 200")
        void should_return_200_with_role_filter() throws Exception {
            when(handler.handle(any())).thenReturn(emptyPage());

            mockMvc.perform(get("/api/v1/campuses/{campusId}/users", CAMPUS_ID)
                            .with(adminJwt())
                            .param("role", "CHANGE_LEAD"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("custom pagination params are accepted — returns 200")
        void should_return_200_with_custom_pagination() throws Exception {
            UserPageResponse customPage = new UserPageResponse(List.of(), 1, 5, 0, 0, true);
            when(handler.handle(any())).thenReturn(customPage);

            mockMvc.perform(get("/api/v1/campuses/{campusId}/users", CAMPUS_ID)
                            .with(adminJwt())
                            .param("page", "1")
                            .param("size", "5"))
                    .andExpect(status().isOk());
        }
    }

    // =========================================================================
    // Security — 401
    // =========================================================================

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no Bearer token → 401")
        void should_return_401_when_no_token() throws Exception {
            mockMvc.perform(get("/api/v1/campuses/{campusId}/users", CAMPUS_ID))
                    .andExpect(status().isUnauthorized());

            verifyNoInteractions(handler);
        }
    }
}
