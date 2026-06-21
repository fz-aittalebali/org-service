package org.dxc.orgservice.shared.domain.entities;

import org.dxc.orgservice.shared.domain.events.IDomainEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for all Aggregate Roots in every bounded context.
 *
 * <p>Accumulates domain events raised during a business operation via
 * {@link #raiseDomainEvents}. The application layer pulls them after each
 * successful command with {@link #pullDomainEvents()} and forwards them to
 * the Outbox. The list is then cleared.
 *
 * <p>No Spring, no Lombok — this class belongs to the domain layer and has
 * zero infrastructure dependencies.
 *
 * @param <ID> the type of the aggregate's primary identifier (e.g. {@code UUID})
 */
public abstract class AggregateRoot<ID> {

    protected final ID id;
    private final List<IDomainEvent> domainEvents;

    protected AggregateRoot(ID id) {
        this.id = id;
        this.domainEvents = new ArrayList<>();
    }

    public ID getId() {
        return id;
    }

    /**
     * Raises one or more domain events within the current business operation.
     *
     * @param events domain events to accumulate
     */
    public void raiseDomainEvents(IDomainEvent... events) {
        Arrays.stream(events).forEach(domainEvents::add);
    }

    /**
     * Returns an immutable snapshot of all accumulated domain events and clears
     * the internal list.
     *
     * @return ordered, immutable list of domain events (may be empty)
     */
    public List<IDomainEvent> pullDomainEvents() {
        List<IDomainEvent> snapshot = List.copyOf(domainEvents);
        domainEvents.clear();
        return snapshot;
    }
}
