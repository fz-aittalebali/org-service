package org.dxc.orgservice.shared.application.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenAccessException extends AbstractApplicationException {

    public ForbiddenAccessException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
