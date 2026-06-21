package org.dxc.orgservice.department.features.commands.delete_department.application;

import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.department.domain.valueobjects.CampusId;
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

@DisplayName("DeleteDepartmentHandler")
class DeleteDepartmentHandlerTest {

    private IDepartmentRepository   departmentRepository;
    private IEventPublisher         eventPublisher;
    private DeleteDepartmentHandler sut;

    private static final UUID     DEPT_ID   = UUID.randomUUID();
    private static final OrgName  NAME      = OrgName.of("Engineering");
    private static final CampusId CAMPUS_ID = CampusId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        departmentRepository = mock(IDepartmentRepository.class);
        eventPublisher       = mock(IEventPublisher.class);
        sut = new DeleteDepartmentHandler(departmentRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the department exists")
    class WhenExists {

        @BeforeEach
        void stub() {
            when(departmentRepository.findById(DEPT_ID))
                    .thenReturn(Optional.of(Department.reconstitute(DEPT_ID, NAME, CAMPUS_ID, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete() {
            sut.handle(new DeleteDepartmentCommand(DEPT_ID));
            verify(departmentRepository).deleteById(DEPT_ID);
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new DeleteDepartmentCommand(DEPT_ID));
            verify(eventPublisher).publishAll(any());
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
            assertThatThrownBy(() -> sut.handle(new DeleteDepartmentCommand(DEPT_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when not found")
        void should_not_delete() {
            try { sut.handle(new DeleteDepartmentCommand(DEPT_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(departmentRepository, never()).deleteById(any());
        }
    }
}
