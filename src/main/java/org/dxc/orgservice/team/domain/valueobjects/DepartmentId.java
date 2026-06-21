package org.dxc.orgservice.team.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.UUID;

public final class DepartmentId extends ValueObject {

    private final UUID value;

    private DepartmentId(UUID value) {
        this.value = value;
    }

    public static DepartmentId of(UUID value) {
        if (value == null)
            throw new InvalidValueObjectException("DepartmentId must not be null");
        return new DepartmentId(value);
    }

    public UUID value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value.toString(); }
}
