package org.dxc.orgservice.campus.features.commands.delete_campus.application;

import org.dxc.orgservice.campus.domain.entities.Campus;
import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.campus.features.commands.delete_campus.application.port.in.IDeleteCampusHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteCampusHandler implements IDeleteCampusHandler {

    private final ICampusRepository campusRepository;
    private final IEventPublisher eventPublisher;

    public DeleteCampusHandler(ICampusRepository campusRepository, IEventPublisher eventPublisher) {
        this.campusRepository = campusRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteCampusCommand command) {
        Campus campus = campusRepository.findById(command.campusId())
                .orElseThrow(() -> new ResourceNotFoundException("Campus", command.campusId()));
        campus.delete();
        campusRepository.deleteById(command.campusId());
        eventPublisher.publishAll(campus.pullDomainEvents());
    }
}
