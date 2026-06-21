package org.dxc.orgservice.city.features.commands.delete_city.application.port.in;

import org.dxc.orgservice.city.features.commands.delete_city.application.DeleteCityCommand;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;

public interface IDeleteCityHandler extends IVoidCommandHandler<DeleteCityCommand> {}
