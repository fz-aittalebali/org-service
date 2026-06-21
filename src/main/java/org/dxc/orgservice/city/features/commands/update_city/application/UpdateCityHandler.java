package org.dxc.orgservice.city.features.commands.update_city.application;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.features.commands.update_city.application.port.in.IUpdateCityHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateCityHandler implements IUpdateCityHandler {

    private final ICityRepository cityRepository;
    private final IEventPublisher eventPublisher;

    public UpdateCityHandler(ICityRepository cityRepository, IEventPublisher eventPublisher) {
        this.cityRepository = cityRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateCityCommand command) {
        City city = cityRepository.findById(command.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", command.cityId()));
        city.update(command.newName(), command.newZipCode());
        cityRepository.save(city);
        eventPublisher.publishAll(city.pullDomainEvents());
    }
}
