package org.dxc.orgservice.company.domain.entities;

import org.dxc.orgservice.company.domain.events.CompanyCreatedEvent;
import org.dxc.orgservice.company.domain.events.CompanyDeletedEvent;
import org.dxc.orgservice.company.domain.events.CompanyUpdatedEvent;
import org.dxc.orgservice.shared.domain.events.IDomainEvent;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Company Aggregate Root")
class CompanyTest {

    private static final OrgName NAME = OrgName.of("DXC Technology");

    private static Company buildNew() {
        return Company.createCompanyBuilder().name(NAME).build();
    }

    private static Company reconstitute() {
        return Company.reconstitute(UUID.randomUUID(), NAME, Instant.now());
    }

    @Nested
    @DisplayName("createCompanyBuilder().build() — construction")
    class WhenCreatedViaBuilder {

        private Company sut;

        @BeforeEach
        void setUp() { sut = buildNew(); }

        @Test
        @DisplayName("should generate a non-null UUID")
        void should_generate_id() { assertThat(sut.getId()).isNotNull(); }

        @Test
        @DisplayName("should store the provided name")
        void should_store_name() { assertThat(sut.getName()).isEqualTo(NAME); }

        @Test
        @DisplayName("should raise exactly one CompanyCreatedEvent")
        void should_raise_created_event() {
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            CompanyCreatedEvent event = (CompanyCreatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(NAME.value());
        }
    }

    @Nested
    @DisplayName("reconstitute() — restoration from persistence")
    class WhenReconstituted {

        @Test
        @DisplayName("should restore all fields without raising events")
        void should_restore_without_events() {
            UUID id = UUID.randomUUID();
            Instant created = Instant.now();
            Company sut = Company.reconstitute(id, NAME, created);

            assertThat(sut.getId()).isEqualTo(id);
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCreatedAt()).isEqualTo(created);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateName()")
    class WhenUpdateName {

        @Test
        @DisplayName("should change name and raise CompanyUpdatedEvent when name differs")
        void should_update_and_raise_event() {
            Company sut = reconstitute();
            OrgName newName = OrgName.of("Accenture");

            sut.updateName(newName);

            assertThat(sut.getName()).isEqualTo(newName);
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CompanyUpdatedEvent.class);
        }

        @Test
        @DisplayName("should raise no event when name is identical")
        void should_not_raise_when_same_name() {
            Company sut = reconstitute();
            sut.updateName(NAME);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class WhenDelete {

        @Test
        @DisplayName("should raise exactly one CompanyDeletedEvent")
        void should_raise_deleted_event() {
            Company sut = reconstitute();
            sut.delete();
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CompanyDeletedEvent.class);
        }
    }
}
