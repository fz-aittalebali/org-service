package org.dxc.orgservice.shared.application.ports.in;

public interface IReturnCommandHandler<C, R> {
    R handle(C command);
}
