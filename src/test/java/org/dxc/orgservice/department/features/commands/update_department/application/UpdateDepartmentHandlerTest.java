package org.dxc.orgservice.department.features.commands.update_department.application;

import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
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

@DisplayName("UpdateDepartmentHandler")
class UpdateDepartmentHandlerTest {

    private IDepartmentRepository    departmentRepository;
    private IEventPublisher          eventPublisher;
    private UpdateDepartmentHandler  sut;

    private static final UUID     DEPT_ID   = UUID.randomUUID();
    private static final OrgName  OLD_NAME  = OrgName.of("Engineering");
    private static final OrgName  NEW_NAME  = OrgName.of("Design");
    private static final CampusId CAMPUS_ID = CampusId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        departmentRepository = mock(IDepartmentRepository.class);
        eventPublisher       = mock(IEventPublisher.class);
        sut = new UpdateDepartmentHandler(departmentRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the update is valid")
    class WhenValidUpdate {

        @BeforeEach
        void stub() {
            when(departmentRepository.findById(DEPT_ID))
                    .thenReturn(Optional.of(Department.reconstitute(DEPT_ID, OLD_NAME, CAMPUS_ID, Instant.now())));
            when(departmentRepository.existsByNameAndCampusId(NEW_NAME, CAMPUS_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save() {
            sut.handle(new UpdateDepartmentCommand(DEPT_ID, NEW_NAME));
            verify(departmentRepository).save(any(Department.class));
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new UpdateDepartmentCommand(DEPT_ID, NEW_NAME));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the new name is already taken")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(departmentRepository.findById(DEPT_ID))
                    .thenReturn(Optional.of(Department.reconstitute(DEPT_ID, OLD_NAME, CAMPUS_ID, Instant.now())));
            when(departmentRepository.existsByNameAndCampusId(NEW_NAME, CAMPUS_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateDepartmentCommand(DEPT_ID, NEW_NAME)))
                    .isInstanceOf(DuplicateResourceException.class);
        }
    }

    @Nested
    @DisplayName("When the department does not exist")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateDepartmentCommand(DEPT_ID, NEW_NAME)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
