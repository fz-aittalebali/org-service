package org.dxc.orgservice.campus.features.commands.update_campus.application;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
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

@DisplayName("UpdateCampusHandler")
class UpdateCampusHandlerTest {

    private ICampusRepository    campusRepository;
    private IEventPublisher      eventPublisher;
    private UpdateCampusHandler  sut;

    private static final UUID      CAMPUS_ID  = UUID.randomUUID();
    private static final OrgName   OLD_NAME   = OrgName.of("Casablanca Campus");
    private static final OrgName   NEW_NAME   = OrgName.of("Rabat Campus");
    private static final CompanyId COMPANY_ID = CompanyId.of(UUID.randomUUID());
    private static final CityId    CITY_ID    = CityId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        campusRepository = mock(ICampusRepository.class);
        eventPublisher   = mock(IEventPublisher.class);
        sut = new UpdateCampusHandler(campusRepository, eventPublisher);
    }

    private Campus stubCampus() {
        return Campus.reconstitute(CAMPUS_ID, OLD_NAME, COMPANY_ID, CITY_ID, Instant.now());
    }

    @Nested
    @DisplayName("When the update is valid")
    class WhenValidUpdate {

        @BeforeEach
        void stub() {
            when(campusRepository.findById(CAMPUS_ID)).thenReturn(Optional.of(stubCampus()));
            when(campusRepository.existsByNameAndCompanyId(NEW_NAME, COMPANY_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save() {
            sut.handle(new UpdateCampusCommand(CAMPUS_ID, NEW_NAME));
            verify(campusRepository).save(any(Campus.class));
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new UpdateCampusCommand(CAMPUS_ID, NEW_NAME));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the new name is already taken")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(campusRepository.findById(CAMPUS_ID)).thenReturn(Optional.of(stubCampus()));
            when(campusRepository.existsByNameAndCompanyId(NEW_NAME, COMPANY_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateCampusCommand(CAMPUS_ID, NEW_NAME)))
                    .isInstanceOf(DuplicateResourceException.class);
        }
    }

    @Nested
    @DisplayName("When the campus does not exist")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(campusRepository.findById(CAMPUS_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateCampusCommand(CAMPUS_ID, NEW_NAME)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
