package org.dxc.orgservice.campus.features.queries.get_users_by_campus.application;

import org.dxc.orgservice.department.adapter.out.persistence.IDepartmentJpaRepository;
import org.dxc.orgservice.shared.application.ports.out.IUserService;
import org.dxc.orgservice.shared.query.dtos.UserPageResponse;
import org.dxc.orgservice.shared.query.dtos.UserSummaryDto;
import org.dxc.orgservice.shared.query.pagination.PageQuery;
import org.dxc.orgservice.team.adapter.out.persistence.ITeamJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("GetUsersByCampusHandler")
class GetUsersByCampusHandlerTest {

    private IDepartmentJpaRepository departmentRepo;
    private ITeamJpaRepository       teamRepo;
    private IUserService             userService;
    private GetUsersByCampusHandler  sut;

    private static final UUID      CAMPUS_ID = UUID.randomUUID();
    private static final UUID      DEPT_ID   = UUID.randomUUID();
    private static final UUID      TEAM_ID   = UUID.randomUUID();
    private static final PageQuery PAGE      = new PageQuery(0, 20);

    @BeforeEach
    void setUp() {
        departmentRepo = mock(IDepartmentJpaRepository.class);
        teamRepo       = mock(ITeamJpaRepository.class);
        userService    = mock(IUserService.class);
        sut = new GetUsersByCampusHandler(departmentRepo, teamRepo, userService);
    }

    private GetUsersByCampusQuery queryWithRole(String role) {
        return new GetUsersByCampusQuery(CAMPUS_ID, role, PAGE);
    }

    // =========================================================================
    // No departments
    // =========================================================================

    @Nested
    @DisplayName("When the campus has no departments")
    class WhenNoDepartments {

        @BeforeEach
        void stubEmpty() {
            when(departmentRepo.findIdsByCampusId(CAMPUS_ID)).thenReturn(List.of());
        }

        @Test
        @DisplayName("should return an empty page without querying teams or user-service")
        void should_return_empty_page_immediately() {
            UserPageResponse result = sut.handle(queryWithRole(null));

            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
            verifyNoInteractions(teamRepo, userService);
        }

        @Test
        @DisplayName("empty page should preserve page/size from the query")
        void should_preserve_page_metadata() {
            PageQuery customPage = new PageQuery(2, 10);
            UserPageResponse result = sut.handle(new GetUsersByCampusQuery(CAMPUS_ID, null, customPage));

            assertThat(result.page()).isEqualTo(2);
            assertThat(result.size()).isEqualTo(10);
        }
    }

    // =========================================================================
    // Departments exist but no teams
    // =========================================================================

    @Nested
    @DisplayName("When departments exist but none has teams")
    class WhenNoTeams {

        @BeforeEach
        void stub() {
            when(departmentRepo.findIdsByCampusId(CAMPUS_ID)).thenReturn(List.of(DEPT_ID));
            when(teamRepo.findIdsByDepartmentIdIn(List.of(DEPT_ID))).thenReturn(List.of());
        }

        @Test
        @DisplayName("should return an empty page without calling user-service")
        void should_return_empty_page_without_calling_user_service() {
            UserPageResponse result = sut.handle(queryWithRole(null));

            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
            verifyNoInteractions(userService);
        }
    }

    // =========================================================================
    // Full path — departments + teams found
    // =========================================================================

    @Nested
    @DisplayName("When departments and teams exist")
    class WhenTeamsExist {

        private final UserPageResponse userServiceResponse = new UserPageResponse(
                List.of(new UserSummaryDto(UUID.randomUUID(), "Alice", "Durand",
                        "alice@dxc.com", "+33600000001", "ACTIVE", Instant.now())),
                0, 20, 1, 1, true);

        @BeforeEach
        void stub() {
            when(departmentRepo.findIdsByCampusId(CAMPUS_ID)).thenReturn(List.of(DEPT_ID));
            when(teamRepo.findIdsByDepartmentIdIn(List.of(DEPT_ID))).thenReturn(List.of(TEAM_ID));
            when(userService.getUsersByTeamIds(anyList(), any(), anyInt(), anyInt()))
                    .thenReturn(userServiceResponse);
        }

        @Test
        @DisplayName("should return the user-service response directly")
        void should_return_user_service_response() {
            UserPageResponse result = sut.handle(queryWithRole(null));

            assertThat(result).isSameAs(userServiceResponse);
        }

        @Test
        @DisplayName("should forward resolved teamIds to user-service")
        void should_forward_team_ids_to_user_service() {
            sut.handle(queryWithRole(null));

            verify(userService).getUsersByTeamIds(List.of(TEAM_ID), null, 0, 20);
        }

        @Test
        @DisplayName("should forward role filter to user-service when role is provided")
        void should_forward_role_to_user_service() {
            sut.handle(queryWithRole("CHANGE_LEAD"));

            verify(userService).getUsersByTeamIds(eq(List.of(TEAM_ID)), eq("CHANGE_LEAD"), anyInt(), anyInt());
        }

        @Test
        @DisplayName("result contains the expected users from user-service")
        void should_contain_expected_users() {
            UserPageResponse result = sut.handle(queryWithRole(null));

            assertThat(result.content()).hasSize(1);
            assertThat(result.content().get(0).firstName()).isEqualTo("Alice");
        }
    }
}
