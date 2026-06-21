package org.dxc.orgservice.team.features.queries.get_team_by_id.application;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.team.features.queries.get_team_by_id.application.port.out.IGetTeamByIdReadModel;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GetTeamByIdHandler")
class GetTeamByIdHandlerTest {

    private IGetTeamByIdReadModel readModel;
    private GetTeamByIdHandler    sut;

    private static final UUID TEAM_ID       = UUID.randomUUID();
    private static final UUID DEPARTMENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        readModel = mock(IGetTeamByIdReadModel.class);
        sut = new GetTeamByIdHandler(readModel);
    }

    @Nested
    @DisplayName("When the team exists")
    class WhenExists {

        private TeamResponse expected;

        @BeforeEach
        void stub() {
            expected = new TeamResponse(TEAM_ID, "Backend", DEPARTMENT_ID, Instant.now());
            when(readModel.findById(TEAM_ID)).thenReturn(Optional.of(expected));
        }

        @Test
        @DisplayName("should return the matching response")
        void should_return_response() {
            TeamResponse result = sut.handle(new GetTeamByIdQuery(TEAM_ID));
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("When the team does not exist")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(readModel.findById(TEAM_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new GetTeamByIdQuery(TEAM_ID)))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Team");
        }
    }
}
