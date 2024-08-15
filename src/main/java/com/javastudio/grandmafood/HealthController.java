package com.javastudio.grandmafood;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Operation(summary = "Check the health of the application")
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
