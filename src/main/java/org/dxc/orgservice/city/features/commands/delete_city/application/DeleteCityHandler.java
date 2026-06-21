package org.dxc.orgservice.city.features.commands.delete_city.application;

import org.dxc.orgservice.city.domain.entities.City;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.city.features.commands.delete_city.application.port.in.IDeleteCityHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteCityHandler implements IDeleteCityHandler {

    private final ICityRepository cityRepository;
    private final IEventPublisher eventPublisher;

    public DeleteCityHandler(ICityRepository cityRepository, IEventPublisher eventPublisher) {
        this.cityRepository = cityRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteCityCommand command) {
        City city = cityRepository.findById(command.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", command.cityId()));
        city.delete();
        cityRepository.deleteById(command.cityId());
        eventPublisher.publishAll(city.pullDomainEvents());
    }
}
