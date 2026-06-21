package org.dxc.orgservice.country.features.queries.get_country_by_id.application;

import org.dxc.orgservice.country.features.queries.get_country_by_id.application.port.out.IGetCountryByIdReadModel;
import org.dxc.orgservice.country.features.queries.shared.dto.CountryResponse;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GetCountryByIdHandler")
class GetCountryByIdHandlerTest {

    private IGetCountryByIdReadModel readModel;
    private GetCountryByIdHandler    sut;

    private static final UUID           COUNTRY_ID = UUID.randomUUID();
    private static final CountryResponse RESPONSE  =
            new CountryResponse(COUNTRY_ID, "Morocco", Instant.now());

    @BeforeEach
    void setUp() {
        readModel = mock(IGetCountryByIdReadModel.class);
        sut = new GetCountryByIdHandler(readModel);
    }

    @Nested
    @DisplayName("When the country exists")
    class WhenCountryExists {

        @BeforeEach
        void stub() {
            when(readModel.findById(COUNTRY_ID)).thenReturn(Optional.of(RESPONSE));
        }

        @Test
        @DisplayName("should return the CountryResponse from the read model")
        void should_return_response() {
            CountryResponse result = sut.handle(new GetCountryByIdQuery(COUNTRY_ID));
            assertThat(result).isSameAs(RESPONSE);
        }

        @Test
        @DisplayName("should return a response with the correct country ID")
        void should_return_correct_id() {
            CountryResponse result = sut.handle(new GetCountryByIdQuery(COUNTRY_ID));
            assertThat(result.id()).isEqualTo(COUNTRY_ID);
        }
    }

    @Nested
    @DisplayName("When the country does not exist")
    class WhenCountryNotFound {

        @BeforeEach
        void stub() {
            when(readModel.findById(COUNTRY_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw_not_found() {
            assertThatThrownBy(() -> sut.handle(new GetCountryByIdQuery(COUNTRY_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
