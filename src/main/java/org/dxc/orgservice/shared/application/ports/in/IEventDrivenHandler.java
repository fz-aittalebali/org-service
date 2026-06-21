package org.dxc.orgservice.shared.application.ports.in;

public interface IEventDrivenHandler<C> {
    void handle(C command);
}
