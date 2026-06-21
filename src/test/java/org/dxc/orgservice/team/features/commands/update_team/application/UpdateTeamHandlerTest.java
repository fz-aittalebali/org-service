package org.dxc.orgservice.team.features.commands.update_team.application;

import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
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

@DisplayName("UpdateTeamHandler")
class UpdateTeamHandlerTest {

    private ITeamRepository  teamRepository;
    private IEventPublisher  eventPublisher;
    private UpdateTeamHandler sut;

    private static final UUID         TEAM_ID       = UUID.randomUUID();
    private static final OrgName      OLD_NAME      = OrgName.of("Backend");
    private static final OrgName      NEW_NAME      = OrgName.of("Frontend");
    private static final DepartmentId DEPARTMENT_ID = DepartmentId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        teamRepository = mock(ITeamRepository.class);
        eventPublisher = mock(IEventPublisher.class);
        sut = new UpdateTeamHandler(teamRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the update is valid")
    class WhenValidUpdate {

        @BeforeEach
        void stub() {
            when(teamRepository.findById(TEAM_ID))
                    .thenReturn(Optional.of(Team.reconstitute(TEAM_ID, OLD_NAME, DEPARTMENT_ID, Instant.now())));
            when(teamRepository.existsByNameAndDepartmentId(NEW_NAME, DEPARTMENT_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save() {
            sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME));
            verify(teamRepository).save(any(Team.class));
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the new name is already taken in the same department")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(teamRepository.findById(TEAM_ID))
                    .thenReturn(Optional.of(Team.reconstitute(TEAM_ID, OLD_NAME, DEPARTMENT_ID, Instant.now())));
            when(teamRepository.existsByNameAndDepartmentId(NEW_NAME, DEPARTMENT_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME)))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("should not save when name is duplicate")
        void should_not_save() {
            try { sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME)); } catch (DuplicateResourceException ignored) {}
            verify(teamRepository, never()).save(any());
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
            assertThatThrownBy(() -> sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should not save when team not found")
        void should_not_save() {
            try { sut.handle(new UpdateTeamCommand(TEAM_ID, NEW_NAME)); } catch (ResourceNotFoundException ignored) {}
            verify(teamRepository, never()).save(any());
        }
    }
}
