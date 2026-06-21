package org.dxc.orgservice.department.features.commands.create_department.application.port.in;

import org.dxc.orgservice.department.features.commands.create_department.application.CreateDepartmentCommand;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;

import java.util.UUID;

public interface ICreateDepartmentHandler extends IReturnCommandHandler<CreateDepartmentCommand, UUID> {}
