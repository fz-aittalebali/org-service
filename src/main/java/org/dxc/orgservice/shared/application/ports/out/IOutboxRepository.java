package org.dxc.orgservice.shared.application.ports.out;

import java.util.List;
import java.util.UUID;

public interface IOutboxRepository {
    void save(OutboxEvent event);
    List<OutboxEvent> findUnpublished(int limit);
    void markPublished(UUID eventId);
}
