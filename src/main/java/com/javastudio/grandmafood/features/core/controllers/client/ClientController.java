package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientDTO;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientDTOMapper;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientDocumentUtils;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientUpdateDto;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.entities.client.ClientUpdateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientUpdateUseCase;
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
    private final ClientUpdateUseCase clientUpdateUseCase;
    private final ClientDeleteUseCase clientDeleteUseCase;

    private final ClientDTOMapper clientDTOMapper;

    public ClientController(
            ClientCreateUseCase clientCreateUseCase,
            ClientFindUseCase clientFindUseCase,
            ClientUpdateUseCase clientUpdateUseCase,
            ClientDeleteUseCase clientDeleteUseCase,
            ClientDTOMapper clientDTOMapper
    ) {
        this.clientCreateUseCase = clientCreateUseCase;
        this.clientFindUseCase = clientFindUseCase;
        this.clientUpdateUseCase = clientUpdateUseCase;
        this.clientDeleteUseCase = clientDeleteUseCase;
        this.clientDTOMapper = clientDTOMapper;
    }

    @PostMapping("/")
    @Operation(summary = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new client",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))
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
                    description = """
                            Client with the specified document id already exists \n
                            Client with the specified email already exists
                            """,
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ClientDTO> create(@RequestBody ClientCreateDTO clientCreateDTO) {
        ClientCreateInput input = clientDTOMapper.toDomain(clientCreateDTO);
        Client client = clientCreateUseCase.create(input);
        ClientDTO responseModel = clientDTOMapper.domainToDto(client);
        return ResponseEntity.status(201).body(responseModel);
    }

    @GetMapping("/{document}")
    @Operation(summary = "Get a client by document")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Document format is not valid",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ClientDTO> getOne(@PathVariable("document") String document) {
        ClientDocumentUtils.DocumentData documentSeparate = ClientDocumentUtils.separateDocument(document);
        Optional<Client> client = clientFindUseCase.findByDocument(documentSeparate.documentId());
        if (client.isEmpty()) {
            throw new ClientNotFoundException();
        }
        ClientDTO responseModel = clientDTOMapper.domainToDto(client.get());
        return ResponseEntity.ok(responseModel);
    }

    @PutMapping("/{document}")
    @Operation(summary = "update a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "There are no different fields in the request",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ClientDTO> updateByDocument(@PathVariable("document") String document, @RequestBody ClientUpdateDto clientUpdateDto) {
        ClientUpdateInput input = clientDTOMapper.updateDtoToDomain(clientUpdateDto);
        ClientDocumentUtils.DocumentData documentSeparate = ClientDocumentUtils.separateDocument(document);
        clientUpdateUseCase.updateByDocument(documentSeparate.documentId(), input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{document}")
    @Operation(summary = "Delete a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Document format is not valid",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
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
