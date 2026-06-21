package org.dxc.orgservice.team.domain.entities;

import org.dxc.orgservice.team.domain.valueobjects.DepartmentId;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;
import org.dxc.orgservice.team.domain.events.TeamCreatedEvent;
import org.dxc.orgservice.team.domain.events.TeamDeletedEvent;
import org.dxc.orgservice.team.domain.events.TeamUpdatedEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the Team bounded context.
 *
 * <p>Invariants enforced here:
 * <ul>
 *   <li>Name must not be blank — validated by {@link OrgName}.</li>
 *   <li>DepartmentId is a foreign-key reference validated at the application layer
 *       before construction.</li>
 *   <li>DepartmentId is immutable once set.</li>
 *   <li>Domain events are accumulated internally and pulled atomically via
 *       {@link #pullDomainEvents()} after each command.</li>
 *   <li>No Spring, no Lombok, no Jakarta annotations — this class is 100% framework-free.</li>
 * </ul>
 *
 * <h3>Construction</h3>
 * <p>New teams are created via the Step Builder returned by {@link #createTeamBuilder()}.
 * Reconstitution from persistence is done via
 * {@link #reconstitute(UUID, OrgName, DepartmentId, Instant)}.
 */
public final class Team extends AggregateRoot<UUID> {

    private OrgName name;
    private final DepartmentId departmentId;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link TeamCreatedEvent}.
     */
    private Team(OrgName name, DepartmentId departmentId) {
        super(UUID.randomUUID());
        this.name = name;
        this.departmentId = departmentId;
        this.createdAt = Instant.now();
        raiseDomainEvents(new TeamCreatedEvent(this.id, name.value(), departmentId.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private Team(UUID id, OrgName name, DepartmentId departmentId, Instant createdAt) {
        super(id);
        this.name = name;
        this.departmentId = departmentId;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new Team.
     * Returns the first Step of the Step Builder — compile-time enforcement
     * of all mandatory fields before {@code build()} is reachable.
     */
    public static NameStep createTeamBuilder() {
        return new CreateTeamSteps();
    }

    /**
     * Reconstitutes a Team from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static Team reconstitute(UUID id, OrgName name, DepartmentId departmentId, Instant createdAt) {
        return new Team(id, name, departmentId, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void updateName(OrgName newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
            raiseDomainEvents(new TeamUpdatedEvent(this.id, newName.value(), departmentId.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new TeamDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName()              { return name; }
    public DepartmentId getDepartmentId() { return departmentId; }
    public Instant getCreatedAt()         { return createdAt; }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        DepartmentIdStep name(OrgName name);
    }

    public interface DepartmentIdStep {
        TerminalStep departmentId(DepartmentId departmentId);
    }

    public interface TerminalStep {
        Team build();
    }

    public static final class CreateTeamSteps
            implements NameStep, DepartmentIdStep, TerminalStep {

        private OrgName name;
        private DepartmentId departmentId;

        @Override
        public DepartmentIdStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public TerminalStep departmentId(DepartmentId departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        @Override
        public Team build() {
            return new Team(name, departmentId);
        }
    }
}
