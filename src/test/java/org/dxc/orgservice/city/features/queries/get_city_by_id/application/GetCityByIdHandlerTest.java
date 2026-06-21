package org.dxc.orgservice.city.features.queries.get_city_by_id.application;

import org.dxc.orgservice.city.features.queries.get_city_by_id.application.port.out.IGetCityByIdReadModel;
import org.dxc.orgservice.city.features.queries.shared.dto.CityResponse;
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

@DisplayName("GetCityByIdHandler")
class GetCityByIdHandlerTest {

    private IGetCityByIdReadModel readModel;
    private GetCityByIdHandler    sut;

    private static final UUID        CITY_ID  = UUID.randomUUID();
    private static final CityResponse RESPONSE = new CityResponse(
            CITY_ID, "Casablanca", "20000", UUID.randomUUID(), Instant.now());

    @BeforeEach
    void setUp() {
        readModel = mock(IGetCityByIdReadModel.class);
        sut = new GetCityByIdHandler(readModel);
    }

    @Nested
    @DisplayName("When the city exists")
    class WhenCityExists {

        @BeforeEach
        void stub() {
            when(readModel.findById(CITY_ID)).thenReturn(Optional.of(RESPONSE));
        }

        @Test
        @DisplayName("should return the CityResponse from the read model")
        void should_return_response() {
            assertThat(sut.handle(new GetCityByIdQuery(CITY_ID))).isSameAs(RESPONSE);
        }
    }

    @Nested
    @DisplayName("When the city does not exist")
    class WhenCityNotFound {

        @BeforeEach
        void stub() {
            when(readModel.findById(CITY_ID)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException")
        void should_throw_not_found() {
            assertThatThrownBy(() -> sut.handle(new GetCityByIdQuery(CITY_ID)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
