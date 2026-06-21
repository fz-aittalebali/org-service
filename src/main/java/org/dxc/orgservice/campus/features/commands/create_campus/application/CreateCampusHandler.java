package org.dxc.orgservice.campus.features.commands.create_campus.application;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.features.commands.create_campus.application.port.in.ICreateCampusHandler;
import org.dxc.orgservice.company.domain.repository.ICompanyRepository;
import org.dxc.orgservice.city.domain.repository.ICityRepository;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateCampusHandler implements ICreateCampusHandler {

    private final ICampusRepository campusRepository;
    private final ICompanyRepository companyRepository;
    private final ICityRepository cityRepository;
    private final IEventPublisher eventPublisher;

    public CreateCampusHandler(ICampusRepository campusRepository,
                               ICompanyRepository companyRepository,
                               ICityRepository cityRepository,
                               IEventPublisher eventPublisher) {
        this.campusRepository = campusRepository;
        this.companyRepository = companyRepository;
        this.cityRepository = cityRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateCampusCommand command) {
        if (!companyRepository.existsById(command.companyId().value())) {
            throw new ResourceNotFoundException("Company", command.companyId().value());
        }
        if (!cityRepository.existsById(command.cityId().value())) {
            throw new ResourceNotFoundException("City", command.cityId().value());
        }
        if (campusRepository.existsByNameAndCompanyId(command.name(), command.companyId())) {
            throw new DuplicateResourceException("Campus", command.name().value());
        }
        Campus campus = Campus.createCampusBuilder()
                .name(command.name())
                .companyId(command.companyId())
                .cityId(command.cityId())
                .build();
        campusRepository.save(campus);
        eventPublisher.publishAll(campus.pullDomainEvents());
        return campus.getId();
    }
}
