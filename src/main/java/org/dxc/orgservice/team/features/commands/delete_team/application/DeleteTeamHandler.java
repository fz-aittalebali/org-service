package org.dxc.orgservice.team.features.commands.delete_team.application;

import org.dxc.orgservice.team.domain.entities.Team;
import org.dxc.orgservice.team.domain.repository.ITeamRepository;
import org.dxc.orgservice.team.features.commands.delete_team.application.port.in.IDeleteTeamHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.application.ports.out.IEventPublisher;

public class DeleteTeamHandler implements IDeleteTeamHandler {

    private final ITeamRepository teamRepository;
    private final IEventPublisher eventPublisher;

    public DeleteTeamHandler(ITeamRepository teamRepository, IEventPublisher eventPublisher) {
        this.teamRepository = teamRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(DeleteTeamCommand command) {
        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", command.teamId()));
        team.delete();
        teamRepository.deleteById(command.teamId());
        eventPublisher.publishAll(team.pullDomainEvents());
    }
}
