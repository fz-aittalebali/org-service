package org.dxc.orgservice.city.domain.entities;

import org.dxc.orgservice.city.domain.events.CityCreatedEvent;
import org.dxc.orgservice.city.domain.events.CityDeletedEvent;
import org.dxc.orgservice.city.domain.events.CityUpdatedEvent;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
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

@DisplayName("City Aggregate Root")
class CityTest {

    private static final OrgName   NAME       = OrgName.of("Casablanca");
    private static final ZipCode   ZIP        = ZipCode.of("20000");
    private static final CountryId COUNTRY_ID = CountryId.of(UUID.randomUUID());

    private static City buildNew() {
        return City.createCityBuilder()
                .name(NAME)
                .zipCode(ZIP)
                .countryId(COUNTRY_ID)
                .build();
    }

    private static City reconstitute() {
        return City.reconstitute(UUID.randomUUID(), NAME, ZIP, COUNTRY_ID, Instant.now());
    }

    // =========================================================================
    // createCityBuilder
    // =========================================================================

    @Nested
    @DisplayName("createCityBuilder().build() — construction")
    class WhenCreatedViaBuilder {

        private City sut;

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
        @DisplayName("should store the name, zipCode, and countryId provided to the builder")
        void should_store_all_fields() {
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getZipCode()).isEqualTo(ZIP);
            assertThat(sut.getCountryId()).isEqualTo(COUNTRY_ID);
        }

        @Test
        @DisplayName("should raise exactly one CityCreatedEvent with the correct payload")
        void should_raise_city_created_event() {
            List<IDomainEvent> events = sut.pullDomainEvents();

            assertThat(events).hasSize(1);
            CityCreatedEvent event = (CityCreatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(NAME.value());
            assertThat(event.getZipCode()).isEqualTo(ZIP.value());
            assertThat(event.getCountryId()).isEqualTo(COUNTRY_ID.value());
        }
    }

    // =========================================================================
    // reconstitute
    // =========================================================================

    @Nested
    @DisplayName("reconstitute() — restoration from persistence")
    class WhenReconstituted {

        @Test
        @DisplayName("should restore all fields and raise no domain events")
        void should_restore_all_fields_without_events() {
            UUID id = UUID.randomUUID();
            Instant created = Instant.now();

            City sut = City.reconstitute(id, NAME, ZIP, COUNTRY_ID, created);

            assertThat(sut.getId()).isEqualTo(id);
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getZipCode()).isEqualTo(ZIP);
            assertThat(sut.getCountryId()).isEqualTo(COUNTRY_ID);
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
        @DisplayName("should update name and zipCode and raise CityUpdatedEvent when values differ")
        void should_update_and_raise_event() {
            City sut = reconstitute();
            OrgName newName = OrgName.of("Rabat");
            ZipCode newZip  = ZipCode.of("10000");

            sut.update(newName, newZip);

            assertThat(sut.getName()).isEqualTo(newName);
            assertThat(sut.getZipCode()).isEqualTo(newZip);
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CityUpdatedEvent.class);
        }

        @Test
        @DisplayName("should raise no event when name and zipCode are identical")
        void should_not_raise_event_when_unchanged() {
            City sut = reconstitute();

            sut.update(NAME, ZIP);

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
        @DisplayName("should raise exactly one CityDeletedEvent")
        void should_raise_city_deleted_event() {
            City sut = reconstitute();

            sut.delete();

            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CityDeletedEvent.class);
        }
    }

    // =========================================================================
    // pullDomainEvents
    // =========================================================================

    @Nested
    @DisplayName("pullDomainEvents()")
    class WhenPullDomainEvents {

        @Test
        @DisplayName("should clear events after first pull")
        void should_clear_events_after_pull() {
            City sut = buildNew();
            sut.pullDomainEvents();

            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }
}
