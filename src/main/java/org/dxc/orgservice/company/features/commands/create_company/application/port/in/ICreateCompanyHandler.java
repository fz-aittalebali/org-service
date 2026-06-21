package org.dxc.orgservice.company.features.commands.create_company.application.port.in;

import org.dxc.orgservice.company.features.commands.create_company.application.CreateCompanyCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateCompanyHandler extends IReturnCommandHandler<CreateCompanyCommand, UUID> {}
