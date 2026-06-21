package org.dxc.orgservice.department.features.commands.update_department.application;

import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.department.features.commands.update_department.application.port.in.IUpdateDepartmentHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateDepartmentHandler implements IUpdateDepartmentHandler {

    private final IDepartmentRepository departmentRepository;
    private final IEventPublisher eventPublisher;

    public UpdateDepartmentHandler(IDepartmentRepository departmentRepository, IEventPublisher eventPublisher) {
        this.departmentRepository = departmentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateDepartmentCommand command) {
        Department department = departmentRepository.findById(command.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", command.departmentId()));
        if (!department.getName().equals(command.name()) &&
                departmentRepository.existsByNameAndCampusId(command.name(), department.getCampusId())) {
            throw new DuplicateResourceException("Department", command.name().value());
        }
        department.updateName(command.name());
        departmentRepository.save(department);
        eventPublisher.publishAll(department.pullDomainEvents());
    }
}
