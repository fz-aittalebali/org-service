package org.dxc.orgservice.country.features.commands.update_country.application;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.country.features.commands.update_country.application.port.in.IUpdateCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateCountryHandler implements IUpdateCountryHandler {

    private final ICountryRepository countryRepository;
    private final IEventPublisher eventPublisher;

    public UpdateCountryHandler(ICountryRepository countryRepository, IEventPublisher eventPublisher) {
        this.countryRepository = countryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateCountryCommand command) {
        Country country = countryRepository.findById(command.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", command.countryId()));
        country.update(command.newName());
        countryRepository.save(country);
        eventPublisher.publishAll(country.pullDomainEvents());
    }
}
