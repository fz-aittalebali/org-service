package org.dxc.orgservice.department.features.commands.create_department.adapter.in.web;

import org.dxc.orgservice.department.features.commands.create_department.application.port.in.ICreateDepartmentHandler;
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

@WebMvcTest(CreateDepartmentController.class)
@DisplayName("POST /api/v1/departments")
class CreateDepartmentControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateDepartmentHandler handler;

    private static final UUID CREATED_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(
                new CreateDepartmentRequest("Engineering", UUID.randomUUID()));
    }

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201 with Location and id in body")
        void should_return_201() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/departments")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/v1/departments/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));
        }
    }

    @Nested
    @DisplayName("Business errors")
    class BusinessErrors {

        @Test
        @DisplayName("campus not found → 404")
        void should_return_404_when_campus_missing() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new ResourceNotFoundException("Campus", UUID.randomUUID()));

            mockMvc.perform(post("/api/v1/departments")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("duplicate department name → 409")
        void should_return_409_when_duplicate() throws Exception {
            when(handler.handle(any()))
                    .thenThrow(new DuplicateResourceException("Department", "Engineering"));

            mockMvc.perform(post("/api/v1/departments")
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
            mockMvc.perform(post("/api/v1/departments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(post("/api/v1/departments")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
