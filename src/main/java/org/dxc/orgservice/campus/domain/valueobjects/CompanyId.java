package org.dxc.orgservice.campus.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.UUID;

public final class CompanyId extends ValueObject {

    private final UUID value;

    private CompanyId(UUID value) {
        this.value = value;
    }

    public static CompanyId of(UUID value) {
        if (value == null)
            throw new InvalidValueObjectException("CompanyId must not be null");
        return new CompanyId(value);
    }

    public UUID value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value.toString(); }
}
