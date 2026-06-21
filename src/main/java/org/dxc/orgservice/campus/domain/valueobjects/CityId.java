package org.dxc.orgservice.campus.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.UUID;

public final class CityId extends ValueObject {

    private final UUID value;

    private CityId(UUID value) {
        this.value = value;
    }

    public static CityId of(UUID value) {
        if (value == null)
            throw new InvalidValueObjectException("CityId must not be null");
        return new CityId(value);
    }

    public UUID value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value.toString(); }
}
