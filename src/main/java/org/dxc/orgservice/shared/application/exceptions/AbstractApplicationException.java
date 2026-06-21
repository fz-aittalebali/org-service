package org.dxc.orgservice.shared.application.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AbstractApplicationException extends RuntimeException {

    protected AbstractApplicationException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
