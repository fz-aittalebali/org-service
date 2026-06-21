package org.dxc.orgservice.shared.application.ports.in;

public interface IVoidCommandHandler<C> {
    void handle(C command);
}
