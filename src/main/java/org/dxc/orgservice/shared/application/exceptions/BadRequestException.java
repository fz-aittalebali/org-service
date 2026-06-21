package org.dxc.orgservice.shared.application.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractApplicationException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
