package org.dxc.orgservice.campus.domain.entities;

import org.dxc.orgservice.campus.domain.events.CampusCreatedEvent;
import org.dxc.orgservice.campus.domain.events.CampusDeletedEvent;
import org.dxc.orgservice.campus.domain.events.CampusUpdatedEvent;
import org.dxc.orgservice.campus.domain.valueobjects.CityId;
import org.dxc.orgservice.campus.domain.valueobjects.CompanyId;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the Campus bounded context.
 *
 * <p>Invariants enforced here:
 * <ul>
 *   <li>Name must not be blank — validated by {@link OrgName}.</li>
 *   <li>CompanyId and CityId are foreign-key references validated at the application layer
 *       before construction.</li>
 *   <li>CompanyId is immutable once set.</li>
 *   <li>Domain events are accumulated internally and pulled atomically via
 *       {@link #pullDomainEvents()} after each command.</li>
 *   <li>No Spring, no Lombok, no Jakarta annotations — this class is 100% framework-free.</li>
 * </ul>
 *
 * <h3>Construction</h3>
 * <p>New campuses are created via the Step Builder returned by {@link #createCampusBuilder()}.
 * Reconstitution from persistence is done via
 * {@link #reconstitute(UUID, OrgName, CompanyId, CityId, Instant)}.
 */
public final class Campus extends AggregateRoot<UUID> {

    private OrgName name;
    private final CompanyId companyId;
    private CityId cityId;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link CampusCreatedEvent}.
     */
    private Campus(OrgName name, CompanyId companyId, CityId cityId) {
        super(UUID.randomUUID());
        this.name = name;
        this.companyId = companyId;
        this.cityId = cityId;
        this.createdAt = Instant.now();
        raiseDomainEvents(new CampusCreatedEvent(this.id, name.value(), companyId.value(), cityId.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private Campus(UUID id, OrgName name, CompanyId companyId, CityId cityId, Instant createdAt) {
        super(id);
        this.name = name;
        this.companyId = companyId;
        this.cityId = cityId;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new Campus.
     * Returns the first Step of the Step Builder — compile-time enforcement
     * of all mandatory fields before {@code build()} is reachable.
     */
    public static NameStep createCampusBuilder() {
        return new CreateCampusSteps();
    }

    /**
     * Reconstitutes a Campus from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static Campus reconstitute(UUID id, OrgName name, CompanyId companyId, CityId cityId, Instant createdAt) {
        return new Campus(id, name, companyId, cityId, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void updateName(OrgName newName) {
        if (!this.name.equals(newName)) {
            this.name = newName;
            raiseDomainEvents(new CampusUpdatedEvent(this.id, newName.value(), companyId.value(), cityId.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new CampusDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName() {
        return name;
    }

    public CompanyId getCompanyId() {
        return companyId;
    }

    public CityId getCityId() {
        return cityId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        CompanyIdStep name(OrgName name);
    }

    public interface CompanyIdStep {
        CityIdStep companyId(CompanyId companyId);
    }

    public interface CityIdStep {
        TerminalStep cityId(CityId cityId);
    }

    public interface TerminalStep {
        Campus build();
    }

    public static final class CreateCampusSteps
            implements NameStep, CompanyIdStep, CityIdStep, TerminalStep {

        private OrgName name;
        private CompanyId companyId;
        private CityId cityId;

        @Override
        public CompanyIdStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public CityIdStep companyId(CompanyId companyId) {
            this.companyId = companyId;
            return this;
        }

        @Override
        public TerminalStep cityId(CityId cityId) {
            this.cityId = cityId;
            return this;
        }

        @Override
        public Campus build() {
            return new Campus(name, companyId, cityId);
        }
    }
}
