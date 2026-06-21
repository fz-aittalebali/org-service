package org.dxc.orgservice.department.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.UUID;

public final class CampusId extends ValueObject {

    private final UUID value;

    private CampusId(UUID value) {
        this.value = value;
    }

    public static CampusId of(UUID value) {
        if (value == null)
            throw new InvalidValueObjectException("CampusId must not be null");
        return new CampusId(value);
    }

    public UUID value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value.toString(); }
}
