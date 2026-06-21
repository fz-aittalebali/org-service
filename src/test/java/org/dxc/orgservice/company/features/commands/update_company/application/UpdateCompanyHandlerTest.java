package org.dxc.orgservice.company.features.commands.update_company.application;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
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

@DisplayName("UpdateCompanyHandler")
class UpdateCompanyHandlerTest {

    private ICompanyRepository   companyRepository;
    private IEventPublisher      eventPublisher;
    private UpdateCompanyHandler sut;

    private static final UUID    COMPANY_ID = UUID.randomUUID();
    private static final OrgName OLD_NAME   = OrgName.of("DXC Technology");
    private static final OrgName NEW_NAME   = OrgName.of("Accenture");

    @BeforeEach
    void setUp() {
        companyRepository = mock(ICompanyRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new UpdateCompanyHandler(companyRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the company exists and the new name is available")
    class WhenValidUpdate {

        @BeforeEach
        void stub() {
            when(companyRepository.findById(COMPANY_ID))
                    .thenReturn(Optional.of(Company.reconstitute(COMPANY_ID, OLD_NAME, Instant.now())));
            when(companyRepository.existsByName(NEW_NAME)).thenReturn(false);
        }

        @Test
        @DisplayName("should save the updated aggregate")
        void should_save() {
            sut.handle(new UpdateCompanyCommand(COMPANY_ID, NEW_NAME));
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new UpdateCompanyCommand(COMPANY_ID, NEW_NAME));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When the new name is already taken by another company")
    class WhenDuplicateName {

        @BeforeEach
        void stub() {
            when(companyRepository.findById(COMPANY_ID))
                    .thenReturn(Optional.of(Company.reconstitute(COMPANY_ID, OLD_NAME, Instant.now())));
            when(companyRepository.existsByName(NEW_NAME)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateCompanyCommand(COMPANY_ID, NEW_NAME)))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("should never save when duplicate name is detected")
        void should_not_save() {
            try { sut.handle(new UpdateCompanyCommand(COMPANY_ID, NEW_NAME)); } catch (DuplicateResourceException ignored) {}
            verify(companyRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("When the company does not exist")
    class WhenNotFound {

        @BeforeEach
        void stub() {
            when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new UpdateCompanyCommand(COMPANY_ID, NEW_NAME)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
