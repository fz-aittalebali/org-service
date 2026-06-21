package org.dxc.orgservice.campus.features.commands.delete_campus.application;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
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

@DisplayName("DeleteCampusHandler")
class DeleteCampusHandlerTest {

    private ICampusRepository   campusRepository;
    private IEventPublisher     eventPublisher;
    private DeleteCampusHandler sut;

    private static final UUID      CAMPUS_ID  = UUID.randomUUID();
    private static final OrgName   NAME       = OrgName.of("Casablanca Campus");
    private static final CompanyId COMPANY_ID = CompanyId.of(UUID.randomUUID());
    private static final CityId    CITY_ID    = CityId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        campusRepository = mock(ICampusRepository.class);
        eventPublisher   = mock(IEventPublisher.class);
        sut = new DeleteCampusHandler(campusRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the campus exists")
    class WhenCampusExists {

        @BeforeEach
        void stub() {
            when(campusRepository.findById(CAMPUS_ID))
                    .thenReturn(Optional.of(Campus.reconstitute(CAMPUS_ID, NAME, COMPANY_ID, CITY_ID, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete() {
            sut.handle(new DeleteCampusCommand(CAMPUS_ID));
            verify(campusRepository).deleteById(CAMPUS_ID);
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new DeleteCampusCommand(CAMPUS_ID));
            verify(eventPublisher).publishAll(any());
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
            assertThatThrownBy(() -> sut.handle(new DeleteCampusCommand(CAMPUS_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when not found")
        void should_not_delete() {
            try { sut.handle(new DeleteCampusCommand(CAMPUS_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(campusRepository, never()).deleteById(any());
        }
    }
}
