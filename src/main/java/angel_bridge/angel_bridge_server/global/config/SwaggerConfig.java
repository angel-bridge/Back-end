package angel_bridge.angel_bridge_server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // Security Requirement와 Security Scheme 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        // OpenAPI 객체 반환
        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addServersItem(new Server().url("https://api.angelbridge.site").description("Production server"))
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("엔젤브릿지 API")
                .description("Ceos 20기 엔젤브릿지 API")
                .version("1.0.0");
    }
}
