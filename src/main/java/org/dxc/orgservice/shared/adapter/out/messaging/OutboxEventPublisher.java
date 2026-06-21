package org.dxc.orgservice.shared.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dxc.orgservice.country.domain.events.CountryCreatedEvent;
import org.dxc.orgservice.country.domain.events.CountryUpdatedEvent;
import org.dxc.orgservice.country.domain.events.CountryDeletedEvent;
import org.dxc.orgservice.city.domain.events.CityCreatedEvent;
import org.dxc.orgservice.city.domain.events.CityUpdatedEvent;
import org.dxc.orgservice.city.domain.events.CityDeletedEvent;
import org.dxc.orgservice.company.domain.events.CompanyCreatedEvent;
import org.dxc.orgservice.company.domain.events.CompanyUpdatedEvent;
import org.dxc.orgservice.company.domain.events.CompanyDeletedEvent;
import org.dxc.orgservice.campus.domain.events.CampusCreatedEvent;
import org.dxc.orgservice.campus.domain.events.CampusUpdatedEvent;
import org.dxc.orgservice.campus.domain.events.CampusDeletedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentCreatedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentUpdatedEvent;
import org.dxc.orgservice.department.domain.events.DepartmentDeletedEvent;
import org.dxc.orgservice.team.domain.events.TeamCreatedEvent;
import org.dxc.orgservice.team.domain.events.TeamUpdatedEvent;
import org.dxc.orgservice.team.domain.events.TeamDeletedEvent;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;
import org.dxc.orgservice.shared.application.ports.out.IOutboxRepository;
import org.dxc.orgservice.shared.application.ports.out.OutboxEvent;
import org.dxc.orgservice.shared.domain.events.AbstractDomainEvent;
import org.dxc.orgservice.shared.domain.events.IDomainEvent;
import org.dxc.orgservice.shared.messaging.events.integration.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OutboxEventPublisher implements IEventPublisher {

    private final IOutboxRepository outboxRepository;
    private final KafkaTopicsProperties topics;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisher(IOutboxRepository outboxRepository,
                                KafkaTopicsProperties topics,
                                ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.topics = topics;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishAll(List<IDomainEvent> events) {
        events.forEach(this::publish);
    }

    private void publish(IDomainEvent event) {
        MappedEvent mapped = mapEvent(event);
        String aggregateId = (event instanceof AbstractDomainEvent<?> ade && ade.getAggregateRootId() != null)
                ? ade.getAggregateRootId().toString()
                : "unknown";
        try {
            String payload = objectMapper.writeValueAsString(mapped.integration());
            outboxRepository.save(new OutboxEvent(
                    UUID.randomUUID(),
                    mapped.integration().getClass().getSimpleName(),
                    mapped.topic(),
                    aggregateId,
                    payload,
                    event.getOccurredOn()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize integration event: " + mapped.integration().getClass().getSimpleName(), e);
        }
    }

    private record MappedEvent(Object integration, String topic) {}

    private MappedEvent mapEvent(IDomainEvent event) {
        return switch (event) {
            case CountryCreatedEvent e -> new MappedEvent(
                    new CountryCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getOccurredOn()),
                    topics.countryCreated());
            case CountryUpdatedEvent e -> new MappedEvent(
                    new CountryUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getOccurredOn()),
                    topics.countryUpdated());
            case CountryDeletedEvent e -> new MappedEvent(
                    new CountryDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.countryDeleted());
            case CityCreatedEvent e -> new MappedEvent(
                    new CityCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getZipCode(), e.getCountryId(), e.getOccurredOn()),
                    topics.cityCreated());
            case CityUpdatedEvent e -> new MappedEvent(
                    new CityUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getZipCode(), e.getCountryId(), e.getOccurredOn()),
                    topics.cityUpdated());
            case CityDeletedEvent e -> new MappedEvent(
                    new CityDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.cityDeleted());
            case CompanyCreatedEvent e -> new MappedEvent(
                    new CompanyCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getOccurredOn()),
                    topics.companyCreated());
            case CompanyUpdatedEvent e -> new MappedEvent(
                    new CompanyUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getOccurredOn()),
                    topics.companyUpdated());
            case CompanyDeletedEvent e -> new MappedEvent(
                    new CompanyDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.companyDeleted());
            case CampusCreatedEvent e -> new MappedEvent(
                    new CampusCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getCompanyId(), e.getCityId(), e.getOccurredOn()),
                    topics.campusCreated());
            case CampusUpdatedEvent e -> new MappedEvent(
                    new CampusUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getCompanyId(), e.getCityId(), e.getOccurredOn()),
                    topics.campusUpdated());
            case CampusDeletedEvent e -> new MappedEvent(
                    new CampusDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.campusDeleted());
            case DepartmentCreatedEvent e -> new MappedEvent(
                    new DepartmentCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getCampusId(), e.getOccurredOn()),
                    topics.departmentCreated());
            case DepartmentUpdatedEvent e -> new MappedEvent(
                    new DepartmentUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getCampusId(), e.getOccurredOn()),
                    topics.departmentUpdated());
            case DepartmentDeletedEvent e -> new MappedEvent(
                    new DepartmentDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.departmentDeleted());
            case TeamCreatedEvent e -> new MappedEvent(
                    new TeamCreatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getDepartmentId(), e.getOccurredOn()),
                    topics.teamCreated());
            case TeamUpdatedEvent e -> new MappedEvent(
                    new TeamUpdatedIntegrationEvent(e.getAggregateRootId(), e.getName(), e.getDepartmentId(), e.getOccurredOn()),
                    topics.teamUpdated());
            case TeamDeletedEvent e -> new MappedEvent(
                    new TeamDeletedIntegrationEvent(e.getAggregateRootId(), e.getOccurredOn()),
                    topics.teamDeleted());
            default -> throw new IllegalStateException(
                    "No integration event mapping for domain event: " + event.getClass().getName());
        };
    }
}
