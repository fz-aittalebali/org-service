package org.dxc.orgservice.city.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.dxc.orgservice.shared.domain.valueobjects.ValueObject;

import java.util.List;
import java.util.regex.Pattern;

public final class ZipCode extends ValueObject {

    public static final int MAX_LENGTH = 20;
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9 \\-]+$");

    private final String value;

    private ZipCode(String value) {
        this.value = value;
    }

    public static ZipCode of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidValueObjectException("ZipCode must not be null or blank");
        }
        String trimmed = raw.strip();
        if (trimmed.length() > MAX_LENGTH) {
            throw new InvalidValueObjectException("ZipCode must not exceed " + MAX_LENGTH + " characters");
        }
        if (!VALID_PATTERN.matcher(trimmed).matches()) {
            throw new InvalidValueObjectException(
                    "ZipCode may only contain alphanumeric characters, spaces, and hyphens");
        }
        return new ZipCode(trimmed);
    }

    public String value() { return value; }

    @Override
    protected List<Object> getEqualityComponents() { return List.of(value); }

    @Override
    public String toString() { return value; }
}
