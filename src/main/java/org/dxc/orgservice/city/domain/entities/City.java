package org.dxc.orgservice.city.domain.entities;

import org.dxc.orgservice.city.domain.events.CityCreatedEvent;
import org.dxc.orgservice.city.domain.events.CityDeletedEvent;
import org.dxc.orgservice.city.domain.events.CityUpdatedEvent;
import org.dxc.orgservice.city.domain.valueobjects.ZipCode;
import org.dxc.orgservice.city.domain.valueobjects.CountryId;
import org.dxc.orgservice.shared.domain.entities.AggregateRoot;
import org.dxc.orgservice.shared.domain.valueobjects.OrgName;

import java.time.Instant;
import java.util.UUID;

/**
 * Aggregate Root for the City bounded context.
 *
 * <p>Invariants enforced here:
 * <ul>
 *   <li>Name must not be blank — validated by {@link OrgName}.</li>
 *   <li>ZipCode must match the expected format — validated by {@link ZipCode}.</li>
 *   <li>CountryId is immutable once set.</li>
 *   <li>Domain events are accumulated internally and pulled atomically via
 *       {@link #pullDomainEvents()} after each command.</li>
 *   <li>No Spring, no Lombok, no Jakarta annotations — this class is 100% framework-free.</li>
 * </ul>
 *
 * <h3>Construction</h3>
 * <p>New cities are created via the Step Builder returned by {@link #createCityBuilder()}.
 * Reconstitution from persistence is done via
 * {@link #reconstitute(UUID, OrgName, ZipCode, CountryId, Instant)}.
 */
public final class City extends AggregateRoot<UUID> {

    private OrgName name;
    private ZipCode zipCode;
    private final CountryId countryId;
    private final Instant createdAt;

    // =========================================================================
    // Constructors — private, only accessed via static factory methods
    // =========================================================================

    /**
     * Creation constructor — called by the Step Builder.
     * Generates a new UUID, stamps createdAt, raises {@link CityCreatedEvent}.
     */
    private City(OrgName name, ZipCode zipCode, CountryId countryId) {
        super(UUID.randomUUID());
        this.name = name;
        this.zipCode = zipCode;
        this.countryId = countryId;
        this.createdAt = Instant.now();
        raiseDomainEvents(new CityCreatedEvent(this.id, name.value(), zipCode.value(), countryId.value(), this.createdAt));
    }

    /**
     * Reconstitution constructor — called exclusively by the infrastructure mapper.
     * Restores full aggregate state from persistence. No events are raised.
     */
    private City(UUID id, OrgName name, ZipCode zipCode, CountryId countryId, Instant createdAt) {
        super(id);
        this.name = name;
        this.zipCode = zipCode;
        this.countryId = countryId;
        this.createdAt = createdAt;
    }

    // =========================================================================
    // Static factory methods
    // =========================================================================

    /**
     * Entry point for creating a new City.
     * Returns the first Step of the Step Builder — compile-time enforcement
     * of all mandatory fields before {@code build()} is reachable.
     */
    public static NameStep createCityBuilder() {
        return new CreateCitySteps();
    }

    /**
     * Reconstitutes a City from its persisted state.
     * Called exclusively by the JPA mapper — never by domain or application code.
     * No domain events are raised during reconstitution.
     */
    public static City reconstitute(UUID id, OrgName name, ZipCode zipCode, CountryId countryId, Instant createdAt) {
        return new City(id, name, zipCode, countryId, createdAt);
    }

    // =========================================================================
    // Domain operations
    // =========================================================================

    public void update(OrgName newName, ZipCode newZipCode) {
        if (!this.name.equals(newName) || !this.zipCode.equals(newZipCode)) {
            this.name = newName;
            this.zipCode = newZipCode;
            raiseDomainEvents(new CityUpdatedEvent(this.id, newName.value(), newZipCode.value(), countryId.value(), Instant.now()));
        }
    }

    public void delete() {
        raiseDomainEvents(new CityDeletedEvent(this.id, Instant.now()));
    }

    // =========================================================================
    // Getters — read-only access, no setters
    // =========================================================================

    public OrgName getName() {
        return name;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // =========================================================================
    // Step Builder — compile-time enforcement of all mandatory fields
    // =========================================================================

    public interface NameStep {
        ZipCodeStep name(OrgName name);
    }

    public interface ZipCodeStep {
        CountryIdStep zipCode(ZipCode zipCode);
    }

    public interface CountryIdStep {
        TerminalStep countryId(CountryId countryId);
    }

    public interface TerminalStep {
        City build();
    }

    public static final class CreateCitySteps
            implements NameStep, ZipCodeStep, CountryIdStep, TerminalStep {

        private OrgName name;
        private ZipCode zipCode;
        private CountryId countryId;

        @Override
        public ZipCodeStep name(OrgName name) {
            this.name = name;
            return this;
        }

        @Override
        public CountryIdStep zipCode(ZipCode zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        @Override
        public TerminalStep countryId(CountryId countryId) {
            this.countryId = countryId;
            return this;
        }

        @Override
        public City build() {
            return new City(name, zipCode, countryId);
        }
    }
}
