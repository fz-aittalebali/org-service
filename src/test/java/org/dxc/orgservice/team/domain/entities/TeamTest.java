package org.dxc.orgservice.team.domain.entities;

import org.dxc.orgservice.shared.domain.events.IDomainEvent;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.events.TeamCreatedEvent;
import org.dxc.orgservice.team.domain.events.TeamDeletedEvent;
import org.dxc.orgservice.team.domain.events.TeamUpdatedEvent;
import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Team aggregate")
class TeamTest {

    private static final OrgName      NAME          = OrgName.of("Backend");
    private static final DepartmentId DEPARTMENT_ID = DepartmentId.of(UUID.randomUUID());

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("When created via builder")
    class WhenCreated {

        private Team team;

        @BeforeEach
        void build() {
            team = Team.createTeamBuilder()
                    .name(NAME)
                    .departmentId(DEPARTMENT_ID)
                    .build();
        }

        @Test
        @DisplayName("should assign a non-null UUID id")
        void should_have_id() {
            assertThat(team.getId()).isNotNull();
        }

        @Test
        @DisplayName("should store the name")
        void should_have_name() {
            assertThat(team.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("should store the departmentId")
        void should_have_department_id() {
            assertThat(team.getDepartmentId()).isEqualTo(DEPARTMENT_ID);
        }

        @Test
        @DisplayName("should stamp createdAt")
        void should_have_created_at() {
            assertThat(team.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("should raise exactly one TeamCreatedEvent")
        void should_raise_created_event() {
            List<IDomainEvent> events = team.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(TeamCreatedEvent.class);
        }

        @Test
        @DisplayName("created event should carry correct name and departmentId")
        void created_event_payload() {
            TeamCreatedEvent evt = (TeamCreatedEvent) team.pullDomainEvents().get(0);
            assertThat(evt.getName()).isEqualTo(NAME.value());
            assertThat(evt.getDepartmentId()).isEqualTo(DEPARTMENT_ID.value());
        }
    }

    // -------------------------------------------------------------------------
    // Reconstitution
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("When reconstituted from persistence")
    class WhenReconstituted {

        private Team team;
        private final UUID      id        = UUID.randomUUID();
        private final Instant   createdAt = Instant.now();

        @BeforeEach
        void build() {
            team = Team.reconstitute(id, NAME, DEPARTMENT_ID, createdAt);
        }

        @Test
        @DisplayName("should restore all fields")
        void should_restore_fields() {
            assertThat(team.getId()).isEqualTo(id);
            assertThat(team.getName()).isEqualTo(NAME);
            assertThat(team.getDepartmentId()).isEqualTo(DEPARTMENT_ID);
            assertThat(team.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("should raise no domain events")
        void should_have_no_events() {
            assertThat(team.pullDomainEvents()).isEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // updateName
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updateName")
    class UpdateName {

        private Team team;

        @BeforeEach
        void build() {
            team = Team.reconstitute(UUID.randomUUID(), NAME, DEPARTMENT_ID, Instant.now());
            team.pullDomainEvents(); // clear any residual events
        }

        @Test
        @DisplayName("should raise TeamUpdatedEvent when name changes")
        void should_raise_updated_event() {
            OrgName newName = OrgName.of("Frontend");
            team.updateName(newName);
            List<IDomainEvent> events = team.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(TeamUpdatedEvent.class);
        }

        @Test
        @DisplayName("should not raise event when name is unchanged")
        void should_not_raise_event_same_name() {
            team.updateName(NAME);
            assertThat(team.pullDomainEvents()).isEmpty();
        }

        @Test
        @DisplayName("should update the stored name")
        void should_update_name_field() {
            OrgName newName = OrgName.of("Frontend");
            team.updateName(newName);
            assertThat(team.getName()).isEqualTo(newName);
        }
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("delete")
    class Delete {

        private Team team;

        @BeforeEach
        void build() {
            team = Team.reconstitute(UUID.randomUUID(), NAME, DEPARTMENT_ID, Instant.now());
            team.pullDomainEvents();
        }

        @Test
        @DisplayName("should raise TeamDeletedEvent")
        void should_raise_deleted_event() {
            team.delete();
            List<IDomainEvent> events = team.pullDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(TeamDeletedEvent.class);
        }
    }

    // -------------------------------------------------------------------------
    // pullDomainEvents
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("pullDomainEvents")
    class PullDomainEvents {

        @Test
        @DisplayName("should accumulate events across multiple operations")
        void should_accumulate_events() {
            Team team = Team.createTeamBuilder()
                    .name(NAME)
                    .departmentId(DEPARTMENT_ID)
                    .build();
            team.updateName(OrgName.of("DevOps"));
            team.delete();
            assertThat(team.pullDomainEvents()).hasSize(3);
        }

        @Test
        @DisplayName("should clear events after being pulled")
        void should_clear_after_pull() {
            Team team = Team.createTeamBuilder()
                    .name(NAME)
                    .departmentId(DEPARTMENT_ID)
                    .build();
            team.pullDomainEvents();
            assertThat(team.pullDomainEvents()).isEmpty();
        }
    }
}
