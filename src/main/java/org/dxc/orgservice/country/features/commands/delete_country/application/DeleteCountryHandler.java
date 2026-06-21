package org.dxc.orgservice.country.features.commands.delete_country.application;

import org.dxc.orgservice.country.domain.entities.Country;
import org.dxc.orgservice.country.domain.repository.ICountryRepository;
import org.dxc.orgservice.country.features.commands.delete_country.application.port.in.IDeleteCountryHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteCountryHandler implements IDeleteCountryHandler {

    private final ICountryRepository countryRepository;
    private final IEventPublisher eventPublisher;

    public DeleteCountryHandler(ICountryRepository countryRepository, IEventPublisher eventPublisher) {
        this.countryRepository = countryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteCountryCommand command) {
        Country country = countryRepository.findById(command.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", command.countryId()));
        country.delete();
        countryRepository.deleteById(command.countryId());
        eventPublisher.publishAll(country.pullDomainEvents());
    }
}
