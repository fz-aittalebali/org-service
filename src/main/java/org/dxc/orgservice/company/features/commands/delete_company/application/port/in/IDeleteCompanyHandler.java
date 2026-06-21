package org.dxc.orgservice.company.features.commands.delete_company.application.port.in;

import org.dxc.orgservice.company.features.commands.delete_company.application.DeleteCompanyCommand;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;

public interface IDeleteCompanyHandler extends IVoidCommandHandler<DeleteCompanyCommand> {}
