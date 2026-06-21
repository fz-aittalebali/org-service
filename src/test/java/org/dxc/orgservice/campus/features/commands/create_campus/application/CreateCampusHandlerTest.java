package org.dxc.orgservice.campus.features.commands.create_campus.application;

import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
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

@DisplayName("CreateCampusHandler")
class CreateCampusHandlerTest {

    private ICampusRepository  campusRepository;
    private ICompanyRepository companyRepository;
    private ICityRepository    cityRepository;
    private IEventPublisher    eventPublisher;
    private CreateCampusHandler sut;

    private static final OrgName   NAME       = OrgName.of("Casablanca Campus");
    private static final CompanyId COMPANY_ID = CompanyId.of(UUID.randomUUID());
    private static final CityId    CITY_ID    = CityId.of(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        campusRepository  = mock(ICampusRepository.class);
        companyRepository = mock(ICompanyRepository.class);
        cityRepository    = mock(ICityRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new CreateCampusHandler(campusRepository, companyRepository, cityRepository, eventPublisher);
    }

    private CreateCampusCommand validCommand() {
        return new CreateCampusCommand(NAME, COMPANY_ID, CITY_ID);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(companyRepository.existsById(COMPANY_ID.value())).thenReturn(true);
            when(cityRepository.existsById(CITY_ID.value())).thenReturn(true);
            when(campusRepository.existsByNameAndCompanyId(NAME, COMPANY_ID)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            assertThat(sut.handle(validCommand())).isNotNull();
        }

        @Test
        @DisplayName("should save the campus aggregate")
        void should_save() {
            sut.handle(validCommand());
            verify(campusRepository).save(any());
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(validCommand());
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the company does not exist")
    class WhenCompanyNotFound {

        @BeforeEach
        void stub() {
            when(companyRepository.existsById(COMPANY_ID.value())).thenReturn(false);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException for Company")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When the city does not exist")
    class WhenCityNotFound {

        @BeforeEach
        void stub() {
            when(companyRepository.existsById(COMPANY_ID.value())).thenReturn(true);
            when(cityRepository.existsById(CITY_ID.value())).thenReturn(false);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException for City")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("When a campus with the same name exists in the company")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(companyRepository.existsById(COMPANY_ID.value())).thenReturn(true);
            when(cityRepository.existsById(CITY_ID.value())).thenReturn(true);
            when(campusRepository.existsByNameAndCompanyId(NAME, COMPANY_ID)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(validCommand()))
                    .isInstanceOf(DuplicateResourceException.class);
        }
    }
}
