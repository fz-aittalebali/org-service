package org.dxc.orgservice.department.features.commands.update_department.adapter.in.web;

import org.dxc.orgservice.department.features.commands.update_department.application.port.in.IUpdateDepartmentHandler;
import org.dxc.orgservice.shared.application.exceptions.ResourceNotFoundException;
import org.dxc.orgservice.shared.features.AbstractControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateDepartmentController.class)
@DisplayName("PUT /api/v1/departments/{id}")
class UpdateDepartmentControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IUpdateDepartmentHandler handler;

    private static final UUID DEPT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new UpdateDepartmentRequest("Design"));
    }

    @Nested
    @DisplayName("204 No Content")
    class Updated {

        @Test
        @DisplayName("should return 204 on success")
        void should_return_204() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(put("/api/v1/departments/{id}", DEPT_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNoContent());

            verify(handler).handle(any());
        }
    }

    @Nested
    @DisplayName("404 Not Found")
    class NotFound {

        @Test
        @DisplayName("handler throws ResourceNotFoundException → 404")
        void should_return_404() throws Exception {
            doThrow(new ResourceNotFoundException("Department", DEPT_ID)).when(handler).handle(any());

            mockMvc.perform(put("/api/v1/departments/{id}", DEPT_ID)
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security {

        @Test
        @DisplayName("no token → 401")
        void should_return_401() throws Exception {
            mockMvc.perform(put("/api/v1/departments/{id}", DEPT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(put("/api/v1/departments/{id}", DEPT_ID)
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
