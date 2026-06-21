package org.dxc.orgservice.city.features.commands.delete_city.application;

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

@DisplayName("DeleteCityHandler")
class DeleteCityHandlerTest {

    private ICityRepository   cityRepository;
    private IEventPublisher   eventPublisher;
    private DeleteCityHandler sut;

    private static final UUID      CITY_ID    = UUID.randomUUID();
    private static final OrgName   NAME       = OrgName.of("Casablanca");
    private static final ZipCode   ZIP        = ZipCode.of("20000");
    private static final CountryId COUNTRY_ID = CountryId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        cityRepository = mock(ICityRepository.class);
        eventPublisher = mock(IEventPublisher.class);
        sut = new DeleteCityHandler(cityRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the city exists")
    class WhenCityExists {

        @BeforeEach
        void stub() {
            when(cityRepository.findById(CITY_ID))
                    .thenReturn(Optional.of(City.reconstitute(CITY_ID, NAME, ZIP, COUNTRY_ID, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete_by_id() {
            sut.handle(new DeleteCityCommand(CITY_ID));
            verify(cityRepository).deleteById(CITY_ID);
        }

        @Test
        @DisplayName("should publish domain events after deleting")
        void should_publish_events() {
            sut.handle(new DeleteCityCommand(CITY_ID));
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
            assertThatThrownBy(() -> sut.handle(new DeleteCityCommand(CITY_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when the city is not found")
        void should_not_delete_when_not_found() {
            try { sut.handle(new DeleteCityCommand(CITY_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(cityRepository, never()).deleteById(any());
        }
    }
}
