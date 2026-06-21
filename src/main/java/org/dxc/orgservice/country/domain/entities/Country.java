package org.dxc.orgservice.country.domain.entities;

import org.dxc.orgservice.country.domain.events.CountryCreatedEvent;
import org.dxc.orgservice.country.domain.events.CountryDeletedEvent;
import org.dxc.orgservice.country.domain.events.CountryUpdatedEvent;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the Country bounded context.
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
 * <p>New countries are created via the Step Builder returned by {@link #createCountryBuilder()}.
 * Reconstitution from persistence is done via {@link #reconstitute(UUID, OrgName, Instant)}.
 */
public final class Country extends AggregateRoot<UUID> {

    private OrgName name;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link CountryCreatedEvent}.
     */
    private Country(OrgName name) {
        super(UUID.randomUUID());
        this.name = name;
        this.createdAt = Instant.now();
        raiseDomainEvents(new CountryCreatedEvent(this.id, name.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private Country(UUID id, OrgName name, Instant createdAt) {
        super(id);
        this.name = name;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new Country.
     * Returns the first Step of the Step Builder.
     */
    public static NameStep createCountryBuilder() {
        return new CreateCountrySteps();
    }

    /**
     * Reconstitutes a Country from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static Country reconstitute(UUID id, OrgName name, Instant createdAt) {
        return new Country(id, name, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void update(OrgName newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
            raiseDomainEvents(new CountryUpdatedEvent(this.id, newName.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new CountryDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName()      { return name; }
    public Instant getCreatedAt() { return createdAt; }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        TerminalStep name(OrgName name);
    }

    public interface TerminalStep {
        Country build();
    }

    public static final class CreateCountrySteps implements NameStep, TerminalStep {

        private OrgName name;

        @Override
        public TerminalStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public Country build() {
            return new Country(name);
        }
    }
}
