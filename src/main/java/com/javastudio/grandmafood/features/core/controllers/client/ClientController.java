package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "clients")
public class ClientController {

    private final ClientCreateUseCase clientCreateUseCase;
    private final ClientFindUseCase clientFindUseCase;
    private final ClientDeleteUseCase clientDeleteUseCase;

    private final ClientDTOMapper clientDTOMapper;

    public ClientController(
            ClientCreateUseCase clientCreateUseCase,
            ClientFindUseCase clientFindUseCase,
            ClientDeleteUseCase clientDeleteUseCase,
            ClientDTOMapper clientDTOMapper
    ) {
        this.clientCreateUseCase = clientCreateUseCase;
        this.clientFindUseCase = clientFindUseCase;
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
                    description = "Client with the specified document id already exists",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Client with the specified email already exists",
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

    @GetMapping
    @Operation(summary = "Get clients")
    @Parameter(
            in = ParameterIn.QUERY,
            name = "orderBy",
            schema = @Schema(type = "string"),
            description = "Order by 'DOCUMENT', 'NAME' or 'ADDRESS'"
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "direction",
            schema = @Schema(type = "string"),
            description = "direction of sort (ASC or DESC)"
    )
    ResponseEntity<List<ClientDTO>> getMany(
            @Parameter(hidden = true) @RequestParam("orderBy") String orderBy,
            @Parameter(hidden = true) @RequestParam("direction") String direction
    ) {

        List<Client> clients = clientFindUseCase.getClientsSorted(orderBy, direction);
        List<ClientDTO> clientDTOS = clients.stream()
                .map(clientDTOMapper::domainToDto)
                .toList();
        return ResponseEntity.ok(clientDTOS);
    }


    @DeleteMapping("/{document}")
    @Operation(summary = "Delete a client by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
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
