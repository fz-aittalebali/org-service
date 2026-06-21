package org.dxc.orgservice.shared.domain.exceptions;

public abstract class AbstractDomainException extends RuntimeException {

    protected AbstractDomainException(String message) {
        super(message);
    }

    protected AbstractDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
