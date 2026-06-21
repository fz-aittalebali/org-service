package org.dxc.orgservice.team.features.commands.update_team.application;

import org.dxc.orgservice.team.domain.entities.Team;
import org.dxc.orgservice.team.domain.repository.ITeamRepository;
import org.dxc.orgservice.team.features.commands.update_team.application.port.in.IUpdateTeamHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class UpdateTeamHandler implements IUpdateTeamHandler {

    private final ITeamRepository teamRepository;
    private final IEventPublisher eventPublisher;

    public UpdateTeamHandler(ITeamRepository teamRepository, IEventPublisher eventPublisher) {
        this.teamRepository = teamRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateTeamCommand command) {
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", command.teamId()));
        if (!team.getName().equals(command.name()) &&
                teamRepository.existsByNameAndDepartmentId(command.name(), team.getDepartmentId())) {
            throw new DuplicateResourceException("Team", command.name().value());
        }
        team.updateName(command.name());
        teamRepository.save(team);
        eventPublisher.publishAll(team.pullDomainEvents());
    }
}
