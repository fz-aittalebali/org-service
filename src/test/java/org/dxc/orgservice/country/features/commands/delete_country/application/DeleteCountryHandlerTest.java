package org.dxc.orgservice.country.features.commands.delete_country.application;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
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

@DisplayName("DeleteCountryHandler")
class DeleteCountryHandlerTest {

    private ICountryRepository   countryRepository;
    private IEventPublisher      eventPublisher;
    private DeleteCountryHandler sut;

    private static final UUID    COUNTRY_ID = UUID.randomUUID();
    private static final OrgName NAME       = OrgName.of("Morocco");

    @BeforeEach
    void setUp() {
        countryRepository = mock(ICountryRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new DeleteCountryHandler(countryRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the country exists")
    class WhenCountryExists {

        @BeforeEach
        void stub() {
            when(countryRepository.findById(COUNTRY_ID))
                    .thenReturn(Optional.of(Country.reconstitute(COUNTRY_ID, NAME, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete_by_id() {
            sut.handle(new DeleteCountryCommand(COUNTRY_ID));
            verify(countryRepository).deleteById(COUNTRY_ID);
        }

        @Test
        @DisplayName("should publish domain events after deleting")
        void should_publish_events() {
            sut.handle(new DeleteCountryCommand(COUNTRY_ID));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the country does not exist")
    class WhenCountryNotFound {

        @BeforeEach
        void stub() {
            when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw_not_found() {
            assertThatThrownBy(() -> sut.handle(new DeleteCountryCommand(COUNTRY_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when the country is not found")
        void should_not_delete_when_not_found() {
            try { sut.handle(new DeleteCountryCommand(COUNTRY_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(countryRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("should never publish events when the country is not found")
        void should_not_publish_when_not_found() {
            try { sut.handle(new DeleteCountryCommand(COUNTRY_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(eventPublisher, never()).publishAll(any());
        }
    }
}
