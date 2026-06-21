package org.dxc.orgservice.company.features.commands.update_company.adapter.in.web;

import org.dxc.orgservice.company.features.commands.update_company.application.port.in.IUpdateCompanyHandler;
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

@WebMvcTest(UpdateCompanyController.class)
@DisplayName("PUT /api/v1/companies/{id}")
class UpdateCompanyControllerTest extends AbstractControllerTest {

    @MockitoBean
    private IUpdateCompanyHandler handler;

    private static final UUID COMPANY_ID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new UpdateCompanyRequest("Accenture"));
    }

    @Nested
    @DisplayName("204 No Content")
    class Updated {

        @Test
        @DisplayName("should return 204 on success")
        void should_return_204() throws Exception {
            doNothing().when(handler).handle(any());

            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
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
            doThrow(new ResourceNotFoundException("Company", COMPANY_ID)).when(handler).handle(any());

            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
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
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
