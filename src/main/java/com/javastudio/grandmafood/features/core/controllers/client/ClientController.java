package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.common.web.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "clients")
public class ClientController {

    @PostMapping("/")
    @Operation(summary = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new actor",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "An invalid input was provided",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "customer with document number already exists",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ClientResponseModel> create(@RequestBody ClientCreateDTO clientCreateDTO) {
        return ResponseEntity.status(201).body(ClientResponseModel.builder().build());
    }

    @GetMapping("/{document}")
    @Operation(summary = "Get a client by document")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseModel.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "client not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ClientResponseModel> getOne(@PathVariable("document") String document) {
        return ResponseEntity.ok(ClientResponseModel.builder().build());
    }

    @DeleteMapping("/{document}")
    @Operation(summary = "Delete a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    description = "client not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<Void> deleteOne(@PathVariable("document") String document) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
