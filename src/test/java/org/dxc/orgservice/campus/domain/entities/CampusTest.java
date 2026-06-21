package org.dxc.orgservice.campus.domain.entities;

import org.dxc.orgservice.campus.domain.events.CampusCreatedEvent;
import org.dxc.orgservice.campus.domain.events.CampusDeletedEvent;
import org.dxc.orgservice.campus.domain.events.CampusUpdatedEvent;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
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

@DisplayName("Campus Aggregate Root")
class CampusTest {

    private static final OrgName   NAME       = OrgName.of("Casablanca Campus");
    private static final CompanyId COMPANY_ID = CompanyId.of(UUID.randomUUID());
    private static final CityId    CITY_ID    = CityId.of(UUID.randomUUID());

    private static Campus buildNew() {
        return Campus.createCampusBuilder()
                .name(NAME)
                .companyId(COMPANY_ID)
                .cityId(CITY_ID)
                .build();
    }

    private static Campus reconstitute() {
        return Campus.reconstitute(UUID.randomUUID(), NAME, COMPANY_ID, CITY_ID, Instant.now());
    }

    @Nested
    @DisplayName("createCampusBuilder().build() — construction")
    class WhenCreatedViaBuilder {

        private Campus sut;

        @BeforeEach
        void setUp() { sut = buildNew(); }

        @Test
        @DisplayName("should generate a non-null UUID")
        void should_generate_id() { assertThat(sut.getId()).isNotNull(); }

        @Test
        @DisplayName("should store name, companyId, and cityId")
        void should_store_all_fields() {
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCompanyId()).isEqualTo(COMPANY_ID);
            assertThat(sut.getCityId()).isEqualTo(CITY_ID);
        }

        @Test
        @DisplayName("should raise exactly one CampusCreatedEvent with correct payload")
        void should_raise_created_event() {
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            CampusCreatedEvent event = (CampusCreatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(NAME.value());
            assertThat(event.getCompanyId()).isEqualTo(COMPANY_ID.value());
            assertThat(event.getCityId()).isEqualTo(CITY_ID.value());
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
            Campus sut = Campus.reconstitute(id, NAME, COMPANY_ID, CITY_ID, created);

            assertThat(sut.getId()).isEqualTo(id);
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCompanyId()).isEqualTo(COMPANY_ID);
            assertThat(sut.getCityId()).isEqualTo(CITY_ID);
            assertThat(sut.getCreatedAt()).isEqualTo(created);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateName()")
    class WhenUpdateName {

        @Test
        @DisplayName("should update name and raise CampusUpdatedEvent when name differs")
        void should_update_and_raise_event() {
            Campus sut = reconstitute();
            OrgName newName = OrgName.of("Rabat Campus");

            sut.updateName(newName);

            assertThat(sut.getName()).isEqualTo(newName);
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CampusUpdatedEvent.class);
        }

        @Test
        @DisplayName("should raise no event when name is identical")
        void should_not_raise_when_same_name() {
            Campus sut = reconstitute();
            sut.updateName(NAME);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class WhenDelete {

        @Test
        @DisplayName("should raise exactly one CampusDeletedEvent")
        void should_raise_deleted_event() {
            Campus sut = reconstitute();
            sut.delete();
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(CampusDeletedEvent.class);
        }
    }
}
