package org.dxc.orgservice.shared.application.ports.out;

import org.dxc.orgservice.shared.domain.events.IDomainEvent;

import java.util.List;

public interface IEventPublisher {
    void publishAll(List<IDomainEvent> events);
}
