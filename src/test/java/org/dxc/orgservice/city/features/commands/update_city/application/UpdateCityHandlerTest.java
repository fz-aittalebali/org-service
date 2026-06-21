package org.dxc.orgservice.city.features.commands.update_city.application;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
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

@DisplayName("UpdateCityHandler")
class UpdateCityHandlerTest {

    private ICityRepository    cityRepository;
    private IEventPublisher    eventPublisher;
    private UpdateCityHandler  sut;

    private static final UUID      CITY_ID    = UUID.randomUUID();
    private static final OrgName   OLD_NAME   = OrgName.of("Casablanca");
    private static final ZipCode   OLD_ZIP    = ZipCode.of("20000");
    private static final CountryId COUNTRY_ID = CountryId.of(UUID.randomUUID());
    private static final OrgName   NEW_NAME   = OrgName.of("Rabat");
    private static final ZipCode   NEW_ZIP    = ZipCode.of("10000");

    @BeforeEach
    void setUp() {
        cityRepository = mock(ICityRepository.class);
        eventPublisher = mock(IEventPublisher.class);
        sut = new UpdateCityHandler(cityRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the city exists")
    class WhenCityExists {

        @BeforeEach
        void stub() {
            when(cityRepository.findById(CITY_ID))
                    .thenReturn(Optional.of(City.reconstitute(CITY_ID, OLD_NAME, OLD_ZIP, COUNTRY_ID, Instant.now())));
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save() {
            sut.handle(new UpdateCityCommand(CITY_ID, NEW_NAME, NEW_ZIP));
            verify(cityRepository).save(any(City.class));
        }

        @Test
        @DisplayName("should publish domain events after saving")
        void should_publish_events() {
            sut.handle(new UpdateCityCommand(CITY_ID, NEW_NAME, NEW_ZIP));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the city does not exist")
    class WhenCityNotFound {

        @BeforeEach
        void stub() {
            when(cityRepository.findById(CITY_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw_not_found() {
            assertThatThrownBy(() -> sut.handle(new UpdateCityCommand(CITY_ID, NEW_NAME, NEW_ZIP)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never save when the city is not found")
        void should_not_save() {
            try { sut.handle(new UpdateCityCommand(CITY_ID, NEW_NAME, NEW_ZIP)); } catch (ResourceNotFoundException ignored) {}
            verify(cityRepository, never()).save(any());
        }
    }
}
