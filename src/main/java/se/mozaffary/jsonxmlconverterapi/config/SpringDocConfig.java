package se.mozaffary.jsonxmlconverterapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080"))
                .addServersItem(new Server().url("https://my.ittt.se"))
                .info(new Info()
                        .title("JSON-XML Converter API")
                        .version("1.0.0")
                        .description("REST API för att konvertera JSON <-> XML, med stöd för externa URL:er"));

    }
}