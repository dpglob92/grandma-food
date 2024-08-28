package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.usecases.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.ClientDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.ClientFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "clients")
public class ClientController {

    private final ClientCreateUseCase clientCreateUseCase;
    private final ClientFindUseCase clientFindUseCase;
    private final ClientDeleteUseCase clientDeleteUseCase;

    public ClientController(ClientCreateUseCase clientCreateUseCase, ClientFindUseCase clientFindUseCase, ClientDeleteUseCase clientDeleteUseCase) {
        this.clientCreateUseCase = clientCreateUseCase;
        this.clientFindUseCase = clientFindUseCase;
        this.clientDeleteUseCase = clientDeleteUseCase;
    }

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
        ClientCreateInput input = ClientControllerDataMapper.clientToDomain(clientCreateDTO);
        Client client = clientCreateUseCase.create(input);
        ClientResponseModel responseModel = ClientControllerDataMapper.clientResponseModel(client);
        return ResponseEntity.status(201).body(responseModel);
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
        ClientDocumentUtils.DocumentData documentSeparate = ClientDocumentUtils.separateDocument(document);
        Optional<Client> client = clientFindUseCase.findByDocument(documentSeparate.documentId());
        if (client.isEmpty()) {
            throw new ClientNotFoundException();
        }
        ClientResponseModel responseModel = ClientControllerDataMapper.clientResponseModel(client.get());
        return ResponseEntity.ok(responseModel);
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
        ClientDocumentUtils.DocumentData documentSeparate = ClientDocumentUtils.separateDocument(document);
        clientDeleteUseCase.deleteByDocument(documentSeparate.documentId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
