package org.dxc.orgservice.country.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.UUID;

public final class CountryId extends ValueObject {

    private final UUID value;

    private CountryId(UUID value) {
        this.value = value;
    }

    public static CountryId of(UUID value) {
        if (value == null)
            throw new InvalidValueObjectException("CountryId must not be null");
        return new CountryId(value);
    }

    public UUID value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value.toString(); }
}
