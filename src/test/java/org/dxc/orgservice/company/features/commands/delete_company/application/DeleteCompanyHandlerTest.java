package org.dxc.orgservice.company.features.commands.delete_company.application;

import org.dxc.orgservice.company.domain.entities.Company;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
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

@DisplayName("DeleteCompanyHandler")
class DeleteCompanyHandlerTest {

    private ICompanyRepository   companyRepository;
    private IEventPublisher      eventPublisher;
    private DeleteCompanyHandler sut;

    private static final UUID    COMPANY_ID = UUID.randomUUID();
    private static final OrgName NAME       = OrgName.of("DXC Technology");

    @BeforeEach
    void setUp() {
        companyRepository = mock(ICompanyRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new DeleteCompanyHandler(companyRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the company exists")
    class WhenCompanyExists {

        @BeforeEach
        void stub() {
            when(companyRepository.findById(COMPANY_ID))
                    .thenReturn(Optional.of(Company.reconstitute(COMPANY_ID, NAME, Instant.now())));
        }

        @Test
        @DisplayName("should call deleteById with the correct ID")
        void should_delete() {
            sut.handle(new DeleteCompanyCommand(COMPANY_ID));
            verify(companyRepository).deleteById(COMPANY_ID);
        }

        @Test
        @DisplayName("should publish domain events")
        void should_publish() {
            sut.handle(new DeleteCompanyCommand(COMPANY_ID));
            verify(eventPublisher).publishAll(any());
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
            assertThatThrownBy(() -> sut.handle(new DeleteCompanyCommand(COMPANY_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should never call deleteById when not found")
        void should_not_delete() {
            try { sut.handle(new DeleteCompanyCommand(COMPANY_ID)); } catch (ResourceNotFoundException ignored) {}
            verify(companyRepository, never()).deleteById(any());
        }
    }
}
