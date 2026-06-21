package org.dxc.orgservice.city.features.commands.create_city.application;

import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
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

@DisplayName("CreateCityHandler")
class CreateCityHandlerTest {

    private ICityRepository    cityRepository;
    private ICountryRepository countryRepository;
    private IEventPublisher    eventPublisher;
    private CreateCityHandler  sut;

    private static final OrgName   NAME       = OrgName.of("Casablanca");
    private static final ZipCode   ZIP        = ZipCode.of("20000");
    private static final CountryId COUNTRY_ID = CountryId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        cityRepository    = mock(ICityRepository.class);
        countryRepository = mock(ICountryRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new CreateCityHandler(cityRepository, countryRepository, eventPublisher);
    }

    private CreateCityCommand validCommand() {
        return new CreateCityCommand(NAME, ZIP, COUNTRY_ID);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(countryRepository.existsById(COUNTRY_ID.value())).thenReturn(true);
            when(cityRepository.existsByNameAndCountryId(NAME, COUNTRY_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            assertThat(sut.handle(validCommand())).isNotNull();
        }

        @Test
        @DisplayName("should save the city aggregate")
        void should_save_city() {
            sut.handle(validCommand());
            verify(cityRepository).save(any());
        }

        @Test
        @DisplayName("should publish domain events after saving")
        void should_publish_events() {
            sut.handle(validCommand());
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the country does not exist")
    class WhenCountryNotFound {

        @BeforeEach
        void stub() {
            when(countryRepository.existsById(COUNTRY_ID.value())).thenReturn(false);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw_not_found() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never save when the country is missing")
        void should_not_save_when_country_missing() {
            assertThatThrownBy(() -> sut.handle(validCommand()));
            verify(cityRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("When a city with the same name already exists in the country")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(countryRepository.existsById(COUNTRY_ID.value())).thenReturn(true);
            when(cityRepository.existsByNameAndCountryId(NAME, COUNTRY_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw_duplicate() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("should never save when a duplicate is detected")
        void should_not_save_when_duplicate() {
            assertThatThrownBy(() -> sut.handle(validCommand()));
            verify(cityRepository, never()).save(any());
        }
    }
}
