package org.dxc.orgservice.country.features.commands.create_country.application;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.country.features.commands.create_country.application.port.in.ICreateCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateCountryHandler implements ICreateCountryHandler {

    private final ICountryRepository countryRepository;
    private final IEventPublisher eventPublisher;

    public CreateCountryHandler(ICountryRepository countryRepository, IEventPublisher eventPublisher) {
        this.countryRepository = countryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateCountryCommand command) {
        if (countryRepository.existsByName(command.name())) {
            throw new DuplicateResourceException("Country", command.name().value());
        }
        Country country = Country.createCountryBuilder()
                .name(command.name())
                .build();
        countryRepository.save(country);
        eventPublisher.publishAll(country.pullDomainEvents());
        return country.getId();
    }
}
