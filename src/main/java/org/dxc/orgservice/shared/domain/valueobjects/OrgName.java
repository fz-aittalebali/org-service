package org.dxc.orgservice.shared.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;

import java.util.List;

public final class OrgName extends ValueObject {

    public static final int MAX_LENGTH = 150;

    private final String value;

    private OrgName(String value) {
        this.value = value;
    }

    public static OrgName of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidValueObjectException("OrgName must not be null or blank");
        }
        String trimmed = raw.strip();
        if (trimmed.length() > MAX_LENGTH) {
            throw new InvalidValueObjectException("OrgName must not exceed " + MAX_LENGTH + " characters");
        }
        return new OrgName(trimmed);
    }

    public String value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value; }
}
