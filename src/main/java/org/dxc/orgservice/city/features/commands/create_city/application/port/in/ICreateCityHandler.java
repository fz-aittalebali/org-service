package org.dxc.orgservice.city.features.commands.create_city.application.port.in;

import org.dxc.orgservice.city.features.commands.create_city.application.CreateCityCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateCityHandler extends IReturnCommandHandler<CreateCityCommand, UUID> {}
