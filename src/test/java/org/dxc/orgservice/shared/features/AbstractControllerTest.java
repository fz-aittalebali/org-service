package org.dxc.orgservice.shared.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dxc.orgservice.shared.adapter.out.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

/**
 * Base class shared by all MockMvc controller tests in org-service.
 *
 * <ul>
 *   <li>{@code @WebMvcTest} — loads only the web slice (controller, ControllerAdvice, security).</li>
 *   <li>{@code @Import(SecurityConfig.class)} — activates {@code @RolesAllowed} and JWT filter.</li>
 *   <li>{@code @MockitoBean JwtDecoder} — prevents startup failure caused by JWKS discovery.</li>
 *   <li>{@code @MockitoBean JpaMetamodelMappingContext} — satisfies {@code @EnableJpaAuditing}
 *       which {@code @WebMvcTest} cannot provide.</li>
 * </ul>
 */
@Import(SecurityConfig.class)
public abstract class AbstractControllerTest {

    protected static final UUID ADMIN_UUID =
            UUID.fromString("aaaaaaaa-0000-0000-0000-aaaaaaaaaaaa");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    @MockitoBean
    @SuppressWarnings("unused")
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    protected static JwtRequestPostProcessor adminJwt() {
        return jwt()
                .jwt(b -> b
                        .subject(ADMIN_UUID.toString())
                        .claim("realm_access", Map.of("roles", List.of("ADMIN"))))
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    protected static JwtRequestPostProcessor nonAdminJwt() {
        return jwt()
                .jwt(b -> b
                        .subject(UUID.randomUUID().toString())
                        .claim("realm_access", Map.of("roles", List.of("USER"))))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
