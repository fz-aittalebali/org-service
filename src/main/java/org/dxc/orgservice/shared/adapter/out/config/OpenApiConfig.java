package org.dxc.orgservice.shared.adapter.out.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI configuration.
 *
 * <p>Registers an OAuth2 {@link SecurityScheme} using the Authorization Code flow (+ PKCE)
 * against the Keycloak {@code alterx} realm. All endpoints inherit this scheme via a global
 * {@link SecurityRequirement}.
 *
 * <p>The Swagger UI client ({@code swagger-ui}) must be registered in Keycloak as a
 * <em>public</em> client with the redirect URI
 * {@code http://localhost:8083/swagger-ui/oauth2-redirect.html}.
 */
@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "keycloak-oauth2";

    @Value("${KEYCLOAK_BASE_URL:http://localhost:8081}")
    private String keycloakBaseUrl;

    @Value("${KEYCLOAK_REALM:alter-x}")
    private String keycloakRealm;

    @Bean
    public OpenAPI openAPI() {
        String baseRealmUrl = keycloakBaseUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect";

        SecurityScheme oauth2Scheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Authenticate via Keycloak realm **" + keycloakRealm + "**. " +
                             "Use client `swagger-ui` (public). Only users with role **ADMIN** " +
                             "can access the protected org-service endpoints.")
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(baseRealmUrl + "/auth")
                                .tokenUrl(baseRealmUrl + "/token")
                                .scopes(new Scopes()
                                        .addString("openid",  "OpenID Connect scope")
                                        .addString("profile", "User profile information")
                                        .addString("email",   "User email address"))));

        return new OpenAPI()
                .info(new Info()
                        .title("Org Service API")
                        .description("Organizational hierarchy microservice — Country → City → Company → Campus → Department → Team. " +
                                     "All write operations require the **ADMIN** realm role.")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, oauth2Scheme))
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEME_NAME));
    }
}
