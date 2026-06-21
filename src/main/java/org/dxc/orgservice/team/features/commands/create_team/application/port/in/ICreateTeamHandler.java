package org.dxc.orgservice.team.features.commands.create_team.application.port.in;

import org.dxc.orgservice.team.features.commands.create_team.application.CreateTeamCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateTeamHandler extends IReturnCommandHandler<CreateTeamCommand, UUID> {}
