package org.dxc.orgservice.department.domain.entities;

import org.dxc.orgservice.department.domain.valueobjects.CampusId;
import org.dxc.orgservice.department.domain.events.DepartmentCreatedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentDeletedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentUpdatedEvent;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the Department bounded context.
 *
 * <p>Invariants enforced here:
 * <ul>
 *   <li>Name must not be blank — validated by {@link OrgName}.</li>
 *   <li>CampusId is a foreign-key reference validated at the application layer
 *       before construction.</li>
 *   <li>CampusId is immutable once set.</li>
 *   <li>Domain events are accumulated internally and pulled atomically via
 *       {@link #pullDomainEvents()} after each command.</li>
 *   <li>No Spring, no Lombok, no Jakarta annotations — this class is 100% framework-free.</li>
 * </ul>
 *
 * <h3>Construction</h3>
 * <p>New departments are created via the Step Builder returned by {@link #createDepartmentBuilder()}.
 * Reconstitution from persistence is done via
 * {@link #reconstitute(UUID, OrgName, CampusId, Instant)}.
 */
public final class Department extends AggregateRoot<UUID> {

    private OrgName name;
    private final CampusId campusId;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link DepartmentCreatedEvent}.
     */
    private Department(OrgName name, CampusId campusId) {
        super(UUID.randomUUID());
        this.name = name;
        this.campusId = campusId;
        this.createdAt = Instant.now();
        raiseDomainEvents(new DepartmentCreatedEvent(this.id, name.value(), campusId.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private Department(UUID id, OrgName name, CampusId campusId, Instant createdAt) {
        super(id);
        this.name = name;
        this.campusId = campusId;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new Department.
     * Returns the first Step of the Step Builder — compile-time enforcement
     * of all mandatory fields before {@code build()} is reachable.
     */
    public static NameStep createDepartmentBuilder() {
        return new CreateDepartmentSteps();
    }

    /**
     * Reconstitutes a Department from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static Department reconstitute(UUID id, OrgName name, CampusId campusId, Instant createdAt) {
        return new Department(id, name, campusId, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void updateName(OrgName newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
            raiseDomainEvents(new DepartmentUpdatedEvent(this.id, newName.value(), campusId.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new DepartmentDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName()       { return name; }
    public CampusId getCampusId()  { return campusId; }
    public Instant getCreatedAt()  { return createdAt; }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        CampusIdStep name(OrgName name);
    }

    public interface CampusIdStep {
        TerminalStep campusId(CampusId campusId);
    }

    public interface TerminalStep {
        Department build();
    }

    public static final class CreateDepartmentSteps
            implements NameStep, CampusIdStep, TerminalStep {

        private OrgName name;
        private CampusId campusId;

        @Override
        public CampusIdStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public TerminalStep campusId(CampusId campusId) {
            this.campusId = campusId;
            return this;
        }

        @Override
        public Department build() {
            return new Department(name, campusId);
        }
    }
}
