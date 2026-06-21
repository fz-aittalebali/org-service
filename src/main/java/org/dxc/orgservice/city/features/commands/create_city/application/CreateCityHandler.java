package org.dxc.orgservice.city.features.commands.create_city.application;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.features.commands.create_city.application.port.in.ICreateCityHandler;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateCityHandler implements ICreateCityHandler {

    private final ICityRepository cityRepository;
    private final ICountryRepository countryRepository;
    private final IEventPublisher eventPublisher;

    public CreateCityHandler(ICityRepository cityRepository, ICountryRepository countryRepository,
                             IEventPublisher eventPublisher) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateCityCommand command) {
        if (!countryRepository.existsById(command.countryId().value())) {
            throw new ResourceNotFoundException("Country", command.countryId().value());
        }
        if (cityRepository.existsByNameAndCountryId(command.name(), command.countryId())) {
            throw new DuplicateResourceException("City", command.name().value());
        }
        City city = City.createCityBuilder()
                .name(command.name())
                .zipCode(command.zipCode())
                .countryId(command.countryId())
                .build();
        cityRepository.save(city);
        eventPublisher.publishAll(city.pullDomainEvents());
        return city.getId();
    }
}
