package org.dxc.orgservice.company.features.commands.create_company.application;

import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
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

@DisplayName("CreateCompanyHandler")
class CreateCompanyHandlerTest {

    private ICompanyRepository   companyRepository;
    private IEventPublisher      eventPublisher;
    private CreateCompanyHandler sut;

    private static final OrgName NAME = OrgName.of("DXC Technology");

    @BeforeEach
    void setUp() {
        companyRepository = mock(ICompanyRepository.class);
        eventPublisher    = mock(IEventPublisher.class);
        sut = new CreateCompanyHandler(companyRepository, eventPublisher);
    }

    @Nested
    @DisplayName("When the command is valid")
    class WhenCommandIsValid {

        @BeforeEach
        void stub() {
            when(companyRepository.existsByName(NAME)).thenReturn(false);
        }

        @Test
        @DisplayName("should return a non-null UUID")
        void should_return_uuid() {
            assertThat(sut.handle(new CreateCompanyCommand(NAME))).isNotNull();
        }

        @Test
        @DisplayName("should save the company aggregate")
        void should_save() {
            sut.handle(new CreateCompanyCommand(NAME));
            verify(companyRepository).save(any());
        }

        @Test
        @DisplayName("should publish domain events after saving")
        void should_publish() {
            sut.handle(new CreateCompanyCommand(NAME));
            verify(eventPublisher).publishAll(any());
        }
    }

    @Nested
    @DisplayName("When a company with the same name already exists")
    class WhenDuplicate {

        @BeforeEach
        void stub() {
            when(companyRepository.existsByName(NAME)).thenReturn(true);
        }

        @Test
        @DisplayName("should throw DuplicateResourceException")
        void should_throw() {
            assertThatThrownBy(() -> sut.handle(new CreateCompanyCommand(NAME)))
                    .isInstanceOf(DuplicateResourceException.class);
        }

        @Test
        @DisplayName("should never save when duplicate is detected")
        void should_not_save() {
            assertThatThrownBy(() -> sut.handle(new CreateCompanyCommand(NAME)));
            verify(companyRepository, never()).save(any());
        }
    }
}
