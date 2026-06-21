package org.dxc.orgservice.department.features.commands.delete_department.application.port.in;

import org.dxc.orgservice.department.features.commands.delete_department.application.DeleteDepartmentCommand;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;

public interface IDeleteDepartmentHandler extends IVoidCommandHandler<DeleteDepartmentCommand> {}
