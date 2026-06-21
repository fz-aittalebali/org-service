package org.dxc.orgservice.campus.features.commands.create_campus.adapter.in.web;

import org.dxc.orgservice.campus.features.commands.create_campus.application.port.in.ICreateCampusHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreateCampusController.class)
@DisplayName("POST /api/v1/campuses")
class CreateCampusControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateCampusHandler handler;

    private static final UUID CREATED_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(
                new CreateCampusRequest("Casablanca Campus", UUID.randomUUID(), UUID.randomUUID()));
    }

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201 with Location and id in body")
        void should_return_201() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/campuses")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/v1/campuses/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));
        }
    }

    @Nested
    @DisplayName("400 Bad Request")
    class ValidationErrors {

        @Test
        @DisplayName("name blank → 400")
        void should_return_400_when_name_blank() throws Exception {
            mockMvc.perform(post("/api/v1/campuses")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new CreateCampusRequest("", UUID.randomUUID(), UUID.randomUUID()))))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(handler);
        }
    }

    @Nested
    @DisplayName("Business errors")
    class BusinessErrors {

        @Test
        @DisplayName("company not found → 404")
        void should_return_404_when_company_missing() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new ResourceNotFoundException("Company", UUID.randomUUID()));

            mockMvc.perform(post("/api/v1/campuses")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("duplicate campus name → 409")
        void should_return_409_when_duplicate() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new DuplicateResourceException("Campus", "Casablanca Campus"));

            mockMvc.perform(post("/api/v1/campuses")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(post("/api/v1/campuses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(post("/api/v1/campuses")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
