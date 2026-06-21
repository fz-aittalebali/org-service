package org.dxc.orgservice.shared.application.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractApplicationException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " not found with id: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
