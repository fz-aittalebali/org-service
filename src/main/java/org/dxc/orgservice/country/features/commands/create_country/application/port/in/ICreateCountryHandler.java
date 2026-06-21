package org.dxc.orgservice.country.features.commands.create_country.application.port.in;

import org.dxc.orgservice.country.features.commands.create_country.application.CreateCountryCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateCountryHandler extends IReturnCommandHandler<CreateCountryCommand, UUID> {}
