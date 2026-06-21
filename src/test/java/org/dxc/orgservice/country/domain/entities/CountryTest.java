package org.dxc.orgservice.country.domain.entities;

import org.dxc.orgservice.country.domain.events.CountryCreatedEvent;
import org.dxc.orgservice.country.domain.events.CountryDeletedEvent;
import org.dxc.orgservice.country.domain.events.CountryUpdatedEvent;
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

@DisplayName("Country Aggregate Root")
class CountryTest {

    private static final OrgName NAME = OrgName.of("Morocco");

    private static Country buildNew() {
        return Country.createCountryBuilder()
                .name(NAME)
                .build();
    }

    private static Country reconstitute() {
        return Country.reconstitute(UUID.randomUUID(), NAME, Instant.now());
    }

    // =========================================================================
    // createCountryBuilder
    // =========================================================================

    @Nested
    @DisplayName("createCountryBuilder().build() — construction")
    class WhenCreatedViaBuilder {

        private Country sut;

        @BeforeEach
        void setUp() {
            sut = buildNew();
        }

        @Test
        @DisplayName("should generate a non-null UUID")
        void should_generate_non_null_id() {
            assertThat(sut.getId()).isNotNull();
        }

        @Test
        @DisplayName("should store the name provided to the builder")
        void should_store_provided_name() {
            assertThat(sut.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("should stamp a non-null createdAt instant")
        void should_stamp_created_at() {
            assertThat(sut.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("should raise exactly one CountryCreatedEvent with the correct payload")
        void should_raise_country_created_event() {
            List<IDomainEvent> events = sut.pullDomainEvents();

            assertThat(events).hasSize(1);
            CountryCreatedEvent event = (CountryCreatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(NAME.value());
        }
    }

    // =========================================================================
    // reconstitute
    // =========================================================================

    @Nested
    @DisplayName("reconstitute() — restoration from persistence")
    class WhenReconstituted {

        @Test
        @DisplayName("should restore all fields exactly and raise no domain events")
        void should_restore_all_fields_without_events() {
            UUID id = UUID.randomUUID();
            Instant created = Instant.now();

            Country sut = Country.reconstitute(id, NAME, created);

            assertThat(sut.getId()).isEqualTo(id);
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCreatedAt()).isEqualTo(created);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    // =========================================================================
    // update
    // =========================================================================

    @Nested
    @DisplayName("update()")
    class WhenUpdate {

        @Test
        @DisplayName("should change the name and raise CountryUpdatedEvent when the name differs")
        void should_update_name_and_raise_event_when_name_differs() {
            Country sut = reconstitute();
            OrgName newName = OrgName.of("Algeria");

            sut.update(newName);

            assertThat(sut.getName()).isEqualTo(newName);
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            CountryUpdatedEvent event = (CountryUpdatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(newName.value());
        }

        @Test
        @DisplayName("should raise no event when the name is identical to the current one")
        void should_not_raise_event_when_name_is_same() {
            Country sut = reconstitute();

            sut.update(NAME);

            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    // =========================================================================
    // delete
    // =========================================================================

    @Nested
    @DisplayName("delete()")
    class WhenDelete {

        @Test
        @DisplayName("should raise exactly one CountryDeletedEvent with the correct aggregate ID")
        void should_raise_country_deleted_event() {
            Country sut = reconstitute();

            sut.delete();

            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            CountryDeletedEvent event = (CountryDeletedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
        }
    }

    // =========================================================================
    // pullDomainEvents
    // =========================================================================

    @Nested
    @DisplayName("pullDomainEvents()")
    class WhenPullDomainEvents {

        @Test
        @DisplayName("should accumulate events from multiple operations in order")
        void should_accumulate_events_in_order() {
            Country sut = reconstitute();
            sut.update(OrgName.of("Tunisia"));
            sut.delete();

            List<IDomainEvent> events = sut.pullDomainEvents();

            assertThat(events).hasSize(2);
            assertThat(events.get(0)).isInstanceOf(CountryUpdatedEvent.class);
            assertThat(events.get(1)).isInstanceOf(CountryDeletedEvent.class);
        }

        @Test
        @DisplayName("should return empty list on second pull — events are cleared after first pull")
        void should_clear_events_after_first_pull() {
            Country sut = buildNew();
            sut.pullDomainEvents();

            assertThat(sut.pullDomainEvents()).isEmpty();
        }

        @Test
        @DisplayName("should return empty list when no operations were performed after reconstitution")
        void should_return_empty_when_no_operations_performed() {
            Country sut = reconstitute();

            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }
}
