package org.dxc.orgservice.department.features.commands.create_department.application;

import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateDepartmentHandler")
class CreateDepartmentHandlerTest {

    private IDepartmentRepository  departmentRepository;
    private ICampusRepository      campusRepository;
    private IEventPublisher        eventPublisher;
    private CreateDepartmentHandler sut;

    private static final OrgName  NAME      = OrgName.of("Engineering");
    private static final CampusId CAMPUS_ID = CampusId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        departmentRepository = mock(IDepartmentRepository.class);
        campusRepository     = mock(ICampusRepository.class);
        eventPublisher       = mock(IEventPublisher.class);
        sut = new CreateDepartmentHandler(departmentRepository, campusRepository, eventPublisher);
    }

    private CreateDepartmentCommand validCommand() {
        return new CreateDepartmentCommand(NAME, CAMPUS_ID);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(campusRepository.existsById(CAMPUS_ID.value())).thenReturn(true);
            when(departmentRepository.existsByNameAndCampusId(NAME, CAMPUS_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            assertThat(sut.handle(validCommand())).isNotNull();
        }

        @Test
        @DisplayName("should save the department aggregate")
        void should_save() {
            sut.handle(validCommand());
            verify(departmentRepository).save(any());
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(validCommand());
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the campus does not exist")
    class WhenCampusNotFound {

        @BeforeEach
        void stub() {
            when(campusRepository.existsById(CAMPUS_ID.value())).thenReturn(false);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When a department with the same name exists in the campus")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(campusRepository.existsById(CAMPUS_ID.value())).thenReturn(true);
            when(departmentRepository.existsByNameAndCampusId(NAME, CAMPUS_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(DuplicateResourceException.class);
        }
    }
}
