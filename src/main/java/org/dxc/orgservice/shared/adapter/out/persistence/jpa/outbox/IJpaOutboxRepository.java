package org.dxc.orgservice.shared.adapter.out.persistence.jpa.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IJpaOutboxRepository extends JpaRepository<OutboxJpaEntity, UUID> {

    @Query(value = "SELECT e FROM OutboxJpaEntity e WHERE e.published = false ORDER BY e.occurredOn ASC LIMIT :limit")
    List<OutboxJpaEntity> findUnpublished(@Param("limit") int limit);
}
