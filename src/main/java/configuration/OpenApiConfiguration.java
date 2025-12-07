package configuration;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.OAuthScope;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "ISpeak API",
                description = "API relative all'applicazione ISpeak",
                version = "v1"
        )
)
@SecurityScheme(
        securitySchemeName = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                    authorizationUrl = "http://localhost:8080/realms/ispeak/protocol/openid-connect/auth",
                    tokenUrl = "http://localhost:8080/realms/ispeak/protocol/openid-connect/token",
                    scopes = {
                            @OAuthScope(name = "openid")
                    }
                )
        )
)
public class OpenApiConfiguration extends Application {
}
