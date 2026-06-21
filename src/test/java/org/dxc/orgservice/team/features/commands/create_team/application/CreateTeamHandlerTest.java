package org.dxc.orgservice.team.features.commands.create_team.application;

import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateTeamHandler")
class CreateTeamHandlerTest {

    private ITeamRepository       teamRepository;
    private IDepartmentRepository departmentRepository;
    private IEventPublisher       eventPublisher;
    private CreateTeamHandler     sut;

    private static final OrgName      NAME          = OrgName.of("Backend");
    private static final DepartmentId DEPARTMENT_ID = DepartmentId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        teamRepository       = mock(ITeamRepository.class);
        departmentRepository = mock(IDepartmentRepository.class);
        eventPublisher       = mock(IEventPublisher.class);
        sut = new CreateTeamHandler(teamRepository, departmentRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(departmentRepository.existsById(DEPARTMENT_ID.value())).thenReturn(true);
            when(teamRepository.existsByNameAndDepartmentId(NAME, DEPARTMENT_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            UUID result = sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID));
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should save the new team aggregate")
        void should_save() {
            sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID));
            verify(teamRepository).save(any(Team.class));
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the department does not exist")
    class WhenDepartmentNotFound {

        @BeforeEach
        void stub() {
            when(departmentRepository.existsById(DEPARTMENT_ID.value())).thenReturn(false);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID)))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Department");
        }

        @Test
        @DisplayName("should never call save when department not found")
        void should_not_save() {
            try { sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(teamRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("When the team name already exists in the department")
    class WhenDuplicate {

        @BeforeEach
        void stub() {
            when(departmentRepository.existsById(DEPARTMENT_ID.value())).thenReturn(true);
            when(teamRepository.existsByNameAndDepartmentId(NAME, DEPARTMENT_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID)))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Team");
        }

        @Test
        @DisplayName("should never call save on duplicate")
        void should_not_save() {
            try { sut.handle(new CreateTeamCommand(NAME, DEPARTMENT_ID)); } catch (DuplicateResourceException ignored) {}
            verify(teamRepository, never()).save(any());
        }
    }
}
