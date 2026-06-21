package org.dxc.orgservice.shared.domain.valueobjects;

import java.util.List;
import java.util.Objects;

public abstract class ValueObject {

    protected abstract List<Object> getEqualityComponents();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return getEqualityComponents().equals(((ValueObject) o).getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEqualityComponents().toArray());
    }
}
