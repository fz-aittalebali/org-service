package org.dxc.orgservice.shared.application.ports.in;

public interface IQueryHandler<Q, R> {
    R handle(Q query);
}
