package org.dxc.orgservice.country.features.commands.delete_country.application.port.in;

import org.dxc.orgservice.country.features.commands.delete_country.application.DeleteCountryCommand;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;

public interface IDeleteCountryHandler extends IVoidCommandHandler<DeleteCountryCommand> {}
