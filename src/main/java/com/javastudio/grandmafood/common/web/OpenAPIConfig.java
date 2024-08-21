package com.javastudio.grandmafood.common.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Grandma's Food API", description = "API for Grandma's Food Restaurant", version = "v1"))
public class OpenAPIConfig {
}
