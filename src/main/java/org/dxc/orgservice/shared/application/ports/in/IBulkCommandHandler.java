package org.dxc.orgservice.shared.application.ports.in;

public interface IBulkCommandHandler<C, R> {
    R handle(C command);
}
