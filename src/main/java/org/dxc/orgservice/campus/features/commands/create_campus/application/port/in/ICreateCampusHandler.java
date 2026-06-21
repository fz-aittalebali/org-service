package org.dxc.orgservice.campus.features.commands.create_campus.application.port.in;

import org.dxc.orgservice.campus.features.commands.create_campus.application.CreateCampusCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateCampusHandler extends IReturnCommandHandler<CreateCampusCommand, UUID> {}
