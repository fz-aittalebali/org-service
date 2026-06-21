package org.dxc.orgservice.department.features.commands.delete_department.application;

import org.dxc.orgservice.department.domain.entities.Department;
import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.department.features.commands.delete_department.application.port.in.IDeleteDepartmentHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteDepartmentHandler implements IDeleteDepartmentHandler {

    private final IDepartmentRepository departmentRepository;
    private final IEventPublisher eventPublisher;

    public DeleteDepartmentHandler(IDepartmentRepository departmentRepository, IEventPublisher eventPublisher) {
        this.departmentRepository = departmentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteDepartmentCommand command) {
        Department department = departmentRepository.findById(command.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", command.departmentId()));
        department.delete();
        departmentRepository.deleteById(command.departmentId());
        eventPublisher.publishAll(department.pullDomainEvents());
    }
}
