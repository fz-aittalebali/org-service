package org.dxc.orgservice.department.features.commands.create_department.application;

import org.dxc.orgservice.campus.domain.repository.ICampusRepository;
import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.department.features.commands.create_department.application.port.in.ICreateDepartmentHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateDepartmentHandler implements ICreateDepartmentHandler {

    private final IDepartmentRepository departmentRepository;
    private final ICampusRepository campusRepository;
    private final IEventPublisher eventPublisher;

    public CreateDepartmentHandler(IDepartmentRepository departmentRepository,
                                   ICampusRepository campusRepository,
                                   IEventPublisher eventPublisher) {
        this.departmentRepository = departmentRepository;
        this.campusRepository = campusRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateDepartmentCommand command) {
        if (!campusRepository.existsById(command.campusId().value())) {
            throw new ResourceNotFoundException("Campus", command.campusId().value());
        }
        if (departmentRepository.existsByNameAndCampusId(command.name(), command.campusId())) {
            throw new DuplicateResourceException("Department", command.name().value());
        }
        Department department = Department.createDepartmentBuilder()
                .name(command.name())
                .campusId(command.campusId())
                .build();
        departmentRepository.save(department);
        eventPublisher.publishAll(department.pullDomainEvents());
        return department.getId();
    }
}
