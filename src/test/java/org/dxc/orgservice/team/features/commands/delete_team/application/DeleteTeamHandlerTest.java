package org.dxc.orgservice.team.features.commands.delete_team.application;

import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.entities.Team;
import org.dxc.orgservice.team.domain.repository.ITeamRepository;
import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("DeleteTeamHandler")
class DeleteTeamHandlerTest {

    private ITeamRepository  teamRepository;
    private IEventPublisher  eventPublisher;
    private DeleteTeamHandler sut;

    private static final UUID         TEAM_ID       = UUID.randomUUID();
    private static final OrgName      NAME          = OrgName.of("Backend");
    private static final DepartmentId DEPARTMENT_ID = DepartmentId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        teamRepository = mock(ITeamRepository.class);
        eventPublisher = mock(IEventPublisher.class);
        sut = new DeleteTeamHandler(teamRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the team exists")
    class WhenExists {

        @BeforeEach
        void stub() {
            when(teamRepository.findById(TEAM_ID))
                    .thenReturn(Optional.of(Team.reconstitute(TEAM_ID, NAME, DEPARTMENT_ID, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete() {
            sut.handle(new DeleteTeamCommand(TEAM_ID));
            verify(teamRepository).deleteById(TEAM_ID);
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new DeleteTeamCommand(TEAM_ID));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the team does not exist")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new DeleteTeamCommand(TEAM_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when not found")
        void should_not_delete() {
            try { sut.handle(new DeleteTeamCommand(TEAM_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(teamRepository, never()).deleteById(any());
        }
    }
}
