package org.dxc.orgservice.shared.adapter.out.persistence.jpa.outbox;

import org.dxc.orgservice.shared.application.ports.out.IOutboxRepository;
import org.dxc.orgservice.shared.application.ports.out.OutboxEvent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class JpaOutboxRepositoryAdapter implements IOutboxRepository {

    private final IJpaOutboxRepository jpaRepository;

    public JpaOutboxRepositoryAdapter(IJpaOutboxRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(OutboxEvent event) {
        jpaRepository.save(OutboxJpaEntity.builder()
                .id(event.id())
                .eventType(event.eventType())
                .topic(event.topic())
                .aggregateId(event.aggregateId())
                .payload(event.payload())
                .occurredOn(event.occurredOn())
                .published(false)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutboxEvent> findUnpublished(int limit) {
        return jpaRepository.findUnpublished(limit).stream()
                .map(e -> new OutboxEvent(e.getId(), e.getEventType(), e.getTopic(),
                        e.getAggregateId(), e.getPayload(), e.getOccurredOn()))
                .toList();
    }

    @Override
    public void markPublished(UUID eventId) {
        jpaRepository.findById(eventId).ifPresent(e -> {
            e.setPublished(true);
            e.setPublishedAt(Instant.now());
            jpaRepository.save(e);
        });
    }
}
