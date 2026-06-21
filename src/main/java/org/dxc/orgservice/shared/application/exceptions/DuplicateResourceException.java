package org.dxc.orgservice.shared.application.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends AbstractApplicationException {

    public DuplicateResourceException(String resourceName, String value) {
        super(resourceName + " already exists with value: " + value);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
