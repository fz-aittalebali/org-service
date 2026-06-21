package org.dxc.orgservice.country.features.commands.create_country.application;

import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateCountryHandler")
class CreateCountryHandlerTest {

    private ICountryRepository countryRepository;
    private IEventPublisher    eventPublisher;
    private CreateCountryHandler sut;

    private static final OrgName NAME = OrgName.of("Morocco");

    @BeforeEach
    void setUp() {
        countryRepository = mock(ICountryRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new CreateCountryHandler(countryRepository, eventPublisher);
    }

    private CreateCountryCommand validCommand() {
        return new CreateCountryCommand(NAME);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(countryRepository.existsByName(NAME)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            assertThat(sut.handle(validCommand())).isNotNull();
        }

        @Test
        @DisplayName("should save the country aggregate")
        void should_save_country() {
            sut.handle(validCommand());
            verify(countryRepository).save(any());
        }

        @Test
        @DisplayName("should publish domain events after saving")
        void should_publish_events() {
            sut.handle(validCommand());
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When a country with the same name already exists")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(countryRepository.existsByName(NAME)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw_duplicate_exception() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("should never save when a duplicate is detected")
        void should_not_save_when_duplicate() {
            assertThatThrownBy(() -> sut.handle(validCommand()));
            verify(countryRepository, never()).save(any());
        }

        @Test
        @DisplayName("should never publish events when a duplicate is detected")
        void should_not_publish_when_duplicate() {
            assertThatThrownBy(() -> sut.handle(validCommand()));
            verify(eventPublisher, never()).publishAll(any());
        }
    }
}
