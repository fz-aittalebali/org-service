package org.dxc.orgservice.shared.domain.exceptions;

public class InvalidEntityStateTransitionException extends AbstractDomainException {

    public InvalidEntityStateTransitionException(String message) {
        super(message);
    }
}
