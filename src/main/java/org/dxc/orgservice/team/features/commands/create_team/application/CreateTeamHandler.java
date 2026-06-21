package org.dxc.orgservice.team.features.commands.create_team.application;

import org.dxc.orgservice.department.domain.repository.IDepartmentRepository;
import org.dxc.orgservice.team.domain.entities.Team;
import org.dxc.orgservice.team.domain.repository.ITeamRepository;
import org.dxc.orgservice.team.features.commands.create_team.application.port.in.ICreateTeamHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

import java.util.UUID;

public class CreateTeamHandler implements ICreateTeamHandler {

    private final ITeamRepository teamRepository;
    private final IDepartmentRepository departmentRepository;
    private final IEventPublisher eventPublisher;

    public CreateTeamHandler(ITeamRepository teamRepository,
                             IDepartmentRepository departmentRepository,
                             IEventPublisher eventPublisher) {
        this.teamRepository = teamRepository;
        this.departmentRepository = departmentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID handle(CreateTeamCommand command) {
        if (!departmentRepository.existsById(command.departmentId().value())) {
            throw new ResourceNotFoundException("Department", command.departmentId().value());
        }
        if (teamRepository.existsByNameAndDepartmentId(command.name(), command.departmentId())) {
            throw new DuplicateResourceException("Team", command.name().value());
        }
        Team team = Team.createTeamBuilder()
                .name(command.name())
                .departmentId(command.departmentId())
                .build();
        teamRepository.save(team);
        eventPublisher.publishAll(team.pullDomainEvents());
        return team.getId();
    }
}
