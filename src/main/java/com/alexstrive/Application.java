package com.alexstrive;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "${api.title}",
                version = "${api.version}",
                description = "${openapi.description}",
                license = @License(name = "MIT", url = "https://en.wikipedia.org/wiki/MIT_License"),
                contact = @Contact(url = "https://alexstrive.com", name = "Alexey Novopashin", email = "alexei.novopashin@gmail.com")
        )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}