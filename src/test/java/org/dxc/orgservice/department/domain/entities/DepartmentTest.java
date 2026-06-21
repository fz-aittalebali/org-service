package org.dxc.orgservice.department.domain.entities;

import org.dxc.orgservice.department.domain.events.DepartmentCreatedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentDeletedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentUpdatedEvent;
import org.dxc.orgservice.department.domain.valueobjects.CampusId;
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

@DisplayName("Department Aggregate Root")
class DepartmentTest {

    private static final OrgName  NAME      = OrgName.of("Engineering");
    private static final CampusId CAMPUS_ID = CampusId.of(UUID.randomUUID());

    private static Department buildNew() {
        return Department.createDepartmentBuilder()
                .name(NAME)
                .campusId(CAMPUS_ID)
                .build();
    }

    private static Department reconstitute() {
        return Department.reconstitute(UUID.randomUUID(), NAME, CAMPUS_ID, Instant.now());
    }

    @Nested
    @DisplayName("createDepartmentBuilder().build() — construction")
    class WhenCreatedViaBuilder {

        private Department sut;

        @BeforeEach
        void setUp() { sut = buildNew(); }

        @Test
        @DisplayName("should generate a non-null UUID")
        void should_generate_id() { assertThat(sut.getId()).isNotNull(); }

        @Test
        @DisplayName("should store name and campusId")
        void should_store_fields() {
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCampusId()).isEqualTo(CAMPUS_ID);
        }

        @Test
        @DisplayName("should raise exactly one DepartmentCreatedEvent with correct payload")
        void should_raise_created_event() {
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            DepartmentCreatedEvent event = (DepartmentCreatedEvent) events.get(0);
            assertThat(event.getAggregateRootId()).isEqualTo(sut.getId());
            assertThat(event.getName()).isEqualTo(NAME.value());
            assertThat(event.getCampusId()).isEqualTo(CAMPUS_ID.value());
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
            Department sut = Department.reconstitute(id, NAME, CAMPUS_ID, created);

            assertThat(sut.getId()).isEqualTo(id);
            assertThat(sut.getName()).isEqualTo(NAME);
            assertThat(sut.getCampusId()).isEqualTo(CAMPUS_ID);
            assertThat(sut.getCreatedAt()).isEqualTo(created);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateName()")
    class WhenUpdateName {

        @Test
        @DisplayName("should update name and raise DepartmentUpdatedEvent when name differs")
        void should_update_and_raise_event() {
            Department sut = reconstitute();
            OrgName newName = OrgName.of("Design");

            sut.updateName(newName);

            assertThat(sut.getName()).isEqualTo(newName);
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(DepartmentUpdatedEvent.class);
        }

        @Test
        @DisplayName("should raise no event when name is identical")
        void should_not_raise_when_same() {
            Department sut = reconstitute();
            sut.updateName(NAME);
            assertThat(sut.pullDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class WhenDelete {

        @Test
        @DisplayName("should raise exactly one DepartmentDeletedEvent")
        void should_raise_deleted_event() {
            Department sut = reconstitute();
            sut.delete();
            List<IDomainEvent> events = sut.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(DepartmentDeletedEvent.class);
        }
    }
}
