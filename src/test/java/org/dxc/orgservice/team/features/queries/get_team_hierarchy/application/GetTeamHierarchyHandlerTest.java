package org.dxc.orgservice.team.features.queries.get_team_hierarchy.application;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.team.features.queries.get_team_hierarchy.application.port.out.IGetTeamHierarchyReadModel;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamHierarchyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GetTeamHierarchyHandler")
class GetTeamHierarchyHandlerTest {

    private IGetTeamHierarchyReadModel readModel;
    private GetTeamHierarchyHandler    sut;

    private static final UUID TEAM_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        readModel = mock(IGetTeamHierarchyReadModel.class);
        sut = new GetTeamHierarchyHandler(readModel);
    }

    @Nested
    @DisplayName("When the hierarchy exists")
    class WhenExists {

        private TeamHierarchyResponse expected;

        @BeforeEach
        void stub() {
            expected = new TeamHierarchyResponse(
                    TEAM_ID, "Backend",
                    UUID.randomUUID(), "Engineering",
                    UUID.randomUUID(), "HQ",
                    UUID.randomUUID(), "DXC Technology",
                    UUID.randomUUID(), "Casablanca",
                    UUID.randomUUID(), "Morocco"
            );
            when(readModel.findHierarchyByTeamId(TEAM_ID)).thenReturn(Optional.of(expected));
        }

        @Test
        @DisplayName("should return the full hierarchy response")
        void should_return_hierarchy() {
            TeamHierarchyResponse result = sut.handle(new GetTeamHierarchyQuery(TEAM_ID));
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("When the team is not found")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(readModel.findHierarchyByTeamId(TEAM_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new GetTeamHierarchyQuery(TEAM_ID)))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Team");
        }
    }
}
