package org.dxc.orgservice.company.features.commands.create_company.adapter.in.web;

import org.dxc.orgservice.company.features.commands.create_company.application.port.in.ICreateCompanyHandler;
import org.dxc.orgservice.shared.application.exceptions.DuplicateResourceException;
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

@WebMvcTest(CreateCompanyController.class)
@DisplayName("POST /api/v1/companies")
class CreateCompanyControllerTest extends AbstractControllerTest {

    @MockitoBean
    private ICreateCompanyHandler handler;

    private static final UUID CREATED_ID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    private String validBody() throws Exception {
        return objectMapper.writeValueAsString(new CreateCompanyRequest("DXC Technology"));
    }

    @Nested
    @DisplayName("201 Created")
    class Created {

        @Test
        @DisplayName("should return 201 with Location and id in body")
        void should_return_201() throws Exception {
            when(handler.handle(any())).thenReturn(CREATED_ID);

            mockMvc.perform(post("/api/v1/companies")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/v1/companies/" + CREATED_ID)))
                    .andExpect(jsonPath("$.id").value(CREATED_ID.toString()));
        }
    }

    @Nested
    @DisplayName("400 Bad Request — Bean Validation")
    class ValidationErrors {

        @Test
        @DisplayName("name blank → 400")
        void should_return_400_when_name_blank() throws Exception {
            mockMvc.perform(post("/api/v1/companies")
                            .with(adminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CreateCompanyRequest(""))))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(handler);
        }
    }

    @Nested
    @DisplayName("409 Conflict")
    class DuplicateName {

        @Test
        @DisplayName("duplicate company name → 409")
        void should_return_409() throws Exception {
            when(handler.handle(any())).thenThrow(new DuplicateResourceException("Company", "DXC Technology"));

            mockMvc.perform(post("/api/v1/companies")
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
            mockMvc.perform(post("/api/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("non-ADMIN role → 403")
        void should_return_403() throws Exception {
            mockMvc.perform(post("/api/v1/companies")
                            .with(nonAdminJwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validBody()))
                    .andExpect(status().isForbidden());
        }
    }
}
