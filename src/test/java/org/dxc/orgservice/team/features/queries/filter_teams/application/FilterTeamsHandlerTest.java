package org.dxc.orgservice.team.features.queries.filter_teams.application;

import org.dxc.orgservice.team.features.queries.filter_teams.application.port.out.IFilterTeamsReadModel;
import org.dxc.orgservice.team.features.queries.shared.dto.TeamResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FilterTeamsHandler")
class FilterTeamsHandlerTest {

    private IFilterTeamsReadModel readModel;
    private FilterTeamsHandler    sut;

    @BeforeEach
    void setUp() {
        readModel = mock(IFilterTeamsReadModel.class);
        sut = new FilterTeamsHandler(readModel);
    }

    @Nested
    @DisplayName("When teams are present")
    class WhenTeamsPresent {

        @Test
        @DisplayName("should delegate to readModel and return the page")
        void should_return_page() {
            Optional<UUID> departmentId = Optional.of(UUID.randomUUID());
            PageRequest pageable = PageRequest.of(0, 20);
            TeamResponse response = new TeamResponse(UUID.randomUUID(), "Backend", departmentId.get(), Instant.now());
            Page<TeamResponse> expected = new PageImpl<>(List.of(response));
            when(readModel.findAll(departmentId, pageable)).thenReturn(expected);

            Page<TeamResponse> result = sut.handle(new FilterTeamsQuery(departmentId, 0, 20));

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("When no teams match the filter")
    class WhenEmpty {

        @Test
        @DisplayName("should return an empty page")
        void should_return_empty_page() {
            Optional<UUID> departmentId = Optional.empty();
            PageRequest pageable = PageRequest.of(0, 20);
            when(readModel.findAll(departmentId, pageable)).thenReturn(Page.empty());

            Page<TeamResponse> result = sut.handle(new FilterTeamsQuery(departmentId, 0, 20));

            assertThat(result.getContent()).isEmpty();
        }
    }
}
