package org.dxc.orgservice.shared.domain.valueobjects;

import org.dxc.orgservice.shared.domain.exceptions.InvalidValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrgName Value Object")
class OrgNameTest {

    // =========================================================================
    // Happy path
    // =========================================================================

    @Nested
    @DisplayName("OrgName.of() — happy path")
    class WhenInputIsValid {

        @Test
        @DisplayName("should strip surrounding whitespace and store the trimmed value")
        void should_strip_surrounding_whitespace() {
            OrgName sut = OrgName.of("  Morocco  ");

            assertThat(sut.value()).isEqualTo("Morocco");
        }

        @Test
        @DisplayName("should preserve internal whitespace in multi-word names")
        void should_preserve_internal_whitespace() {
            OrgName sut = OrgName.of("North Africa");

            assertThat(sut.value()).isEqualTo("North Africa");
        }

        @Test
        @DisplayName("should accept a name at exactly 150 characters")
        void should_accept_name_at_max_length() {
            String raw = "A".repeat(OrgName.MAX_LENGTH);
            OrgName sut = OrgName.of(raw);

            assertThat(sut.value()).hasSize(OrgName.MAX_LENGTH);
        }

        @Test
        @DisplayName("should consider two OrgNames equal when they hold the same trimmed value")
        void should_be_equal_when_same_value_after_trim() {
            OrgName a = OrgName.of("  Casablanca  ");
            OrgName b = OrgName.of("Casablanca");

            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }

        @Test
        @DisplayName("toString should return the trimmed value")
        void should_return_value_from_to_string() {
            OrgName sut = OrgName.of("Rabat");

            assertThat(sut.toString()).isEqualTo("Rabat");
        }
    }

    // =========================================================================
    // Null / blank rejection
    // =========================================================================

    @Nested
    @DisplayName("OrgName.of() — null and blank rejection")
    class WhenInputIsNullOrBlank {

        @Test
        @DisplayName("should throw InvalidValueObjectException when input is null")
        void should_throw_when_null() {
            assertThatThrownBy(() -> OrgName.of(null))
                    .isInstanceOf(InvalidValueObjectException.class)
                    .hasMessageContaining("null or blank");
        }

        @Test
        @DisplayName("should throw InvalidValueObjectException when input is empty string")
        void should_throw_when_empty() {
            assertThatThrownBy(() -> OrgName.of(""))
                    .isInstanceOf(InvalidValueObjectException.class)
                    .hasMessageContaining("null or blank");
        }

        @Test
        @DisplayName("should throw InvalidValueObjectException when input is whitespace only")
        void should_throw_when_whitespace_only() {
            assertThatThrownBy(() -> OrgName.of("   "))
                    .isInstanceOf(InvalidValueObjectException.class)
                    .hasMessageContaining("null or blank");
        }
    }

    // =========================================================================
    // Length rejection
    // =========================================================================

    @Nested
    @DisplayName("OrgName.of() — length rejection")
    class WhenInputExceedsMaxLength {

        @Test
        @DisplayName("should throw InvalidValueObjectException when name exceeds 150 characters")
        void should_throw_when_exceeds_max_length() {
            String tooLong = "A".repeat(OrgName.MAX_LENGTH + 1);

            assertThatThrownBy(() -> OrgName.of(tooLong))
                    .isInstanceOf(InvalidValueObjectException.class)
                    .hasMessageContaining("150");
        }
    }
}
