package org.dxc.orgservice.campus.features.commands.update_campus.application;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.features.commands.update_campus.application.port.in.IUpdateCampusHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateCampusHandler implements IUpdateCampusHandler {

    private final ICampusRepository campusRepository;
    private final IEventPublisher eventPublisher;

    public UpdateCampusHandler(ICampusRepository campusRepository, IEventPublisher eventPublisher) {
        this.campusRepository = campusRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateCampusCommand command) {
        Campus campus = campusRepository.findById(command.campusId())
                .orElseThrow(() -> new ResourceNotFoundException("Campus", command.campusId()));
        if (!campus.getName().equals(command.name()) &&
                campusRepository.existsByNameAndCompanyId(command.name(), campus.getCompanyId())) {
            throw new DuplicateResourceException("Campus", command.name().value());
        }
        campus.updateName(command.name());
        campusRepository.save(campus);
        eventPublisher.publishAll(campus.pullDomainEvents());
    }
}
