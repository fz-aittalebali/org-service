package org.dxc.orgservice.country.features.commands.update_country.application;

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

@DisplayName("UpdateCountryHandler")
class UpdateCountryHandlerTest {

    private ICountryRepository   countryRepository;
    private IEventPublisher      eventPublisher;
    private UpdateCountryHandler sut;

    private static final UUID    COUNTRY_ID = UUID.randomUUID();
    private static final OrgName OLD_NAME   = OrgName.of("Morocco");
    private static final OrgName NEW_NAME   = OrgName.of("Algeria");

    @BeforeEach
    void setUp() {
        countryRepository = mock(ICountryRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new UpdateCountryHandler(countryRepository, eventPublisher);
    }

    private Country stubCountry() {
        return Country.reconstitute(COUNTRY_ID, OLD_NAME, Instant.now());
    }

    @Nested
    @DisplayName("When the country exists")
    class WhenCountryExists {

        @BeforeEach
        void stub() {
            when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(stubCountry()));
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save_aggregate() {
            sut.handle(new UpdateCountryCommand(COUNTRY_ID, NEW_NAME));
            verify(countryRepository).save(any(Country.class));
        }

        @Test
        @DisplayName("should publish domain events after saving")
        void should_publish_events() {
            sut.handle(new UpdateCountryCommand(COUNTRY_ID, NEW_NAME));
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
            assertThatThrownBy(() -> sut.handle(new UpdateCountryCommand(COUNTRY_ID, NEW_NAME)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never save when the country is not found")
        void should_not_save() {
            try { sut.handle(new UpdateCountryCommand(COUNTRY_ID, NEW_NAME)); } catch (ResourceNotFoundException ignored) {}
            verify(countryRepository, never()).save(any());
        }

        @Test
        @DisplayName("should never publish events when the country is not found")
        void should_not_publish() {
            try { sut.handle(new UpdateCountryCommand(COUNTRY_ID, NEW_NAME)); } catch (ResourceNotFoundException ignored) {}
            verify(eventPublisher, never()).publishAll(any());
        }
    }
}
