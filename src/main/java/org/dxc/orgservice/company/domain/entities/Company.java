package org.dxc.orgservice.company.domain.entities;

import org.dxc.orgservice.company.domain.events.CompanyCreatedEvent;
import org.dxc.orgservice.company.domain.events.CompanyDeletedEvent;
import org.dxc.orgservice.company.domain.events.CompanyUpdatedEvent;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the Company bounded context.
 *
 * <p>Invariants enforced here:
 * <ul>
 *   <li>Name must not be blank — validated by {@link OrgName}.</li>
 *   <li>Domain events are accumulated internally and pulled atomically via
 *       {@link #pullDomainEvents()} after each command.</li>
 *   <li>No Spring, no Lombok, no Jakarta annotations — this class is 100% framework-free.</li>
 * </ul>
 *
 * <h3>Construction</h3>
 * <p>New companies are created via the Step Builder returned by {@link #createCompanyBuilder()}.
 * Reconstitution from persistence is done via {@link #reconstitute(UUID, OrgName, Instant)}.
 */
public final class Company extends AggregateRoot<UUID> {

    private OrgName name;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link CompanyCreatedEvent}.
     */
    private Company(OrgName name) {
        super(UUID.randomUUID());
        this.name = name;
        this.createdAt = Instant.now();
        raiseDomainEvents(new CompanyCreatedEvent(this.id, name.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private Company(UUID id, OrgName name, Instant createdAt) {
        super(id);
        this.name = name;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new Company.
     * Returns the first Step of the Step Builder.
     */
    public static NameStep createCompanyBuilder() {
        return new CreateCompanySteps();
    }

    /**
     * Reconstitutes a Company from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static Company reconstitute(UUID id, OrgName name, Instant createdAt) {
        return new Company(id, name, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void updateName(OrgName newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
            raiseDomainEvents(new CompanyUpdatedEvent(this.id, newName.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new CompanyDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        TerminalStep name(OrgName name);
    }

    public interface TerminalStep {
        Company build();
    }

    public static final class CreateCompanySteps implements NameStep, TerminalStep {

        private OrgName name;

        @Override
        public TerminalStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public Company build() {
            return new Company(name);
        }
    }
}
