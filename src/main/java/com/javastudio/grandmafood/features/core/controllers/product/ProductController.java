package com.javastudio.grandmafood.features.core.controllers.product;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductDTO;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductUpdateDTO;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import com.javastudio.grandmafood.features.core.entities.product.ProductUpdateInput;
import com.javastudio.grandmafood.features.core.usecases.product.ProductCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductUpdateUseCase;
import com.javastudio.grandmafood.features.errors.ParameterNullException;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
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
import java.util.UUID;

@RequestMapping(path = "/api/v1/products")
@RestController
@Tag(name = "products")
public class ProductController {

    private final ProductCreateUseCase createUseCase;
    private final ProductFindUseCase findUseCase;
    private final ProductDTOMapper productDTOMapper;
    private final ProductDeleteUseCase deleteUseCase;
    private final ProductUpdateUseCase updateUseCase;

    public ProductController(
            ProductCreateUseCase createUseCase,
            ProductFindUseCase findUseCase,
            ProductDTOMapper productDTOMapper,
            ProductDeleteUseCase deleteUseCase,
            ProductUpdateUseCase updateUseCase
    ) {
        this.createUseCase = createUseCase;
        this.findUseCase = findUseCase;
        this.productDTOMapper = productDTOMapper;
        this.deleteUseCase = deleteUseCase;
        this.updateUseCase = updateUseCase;
    }

    @PostMapping("/")
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new product",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
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
                    description = "Product with the specified name already exits",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductCreateInput input = productDTOMapper.dtoToDomain(productCreateDTO);
        Product product = createUseCase.create(input);
        return ResponseEntity.status(201).body(productDTOMapper.domainToDTO(product));
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid uuid",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Product with the specified name already exits",
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
    ResponseEntity<ProductDTO> update(@PathVariable("uuid") String uuid, @RequestBody ProductUpdateDTO productUpdateDto) {
        UUID parsedUuid = ValidationUtils.parseUUID(uuid);
        ProductUpdateInput input = productDTOMapper.updateDTOToDomain(productUpdateDto);
        updateUseCase.updateById(parsedUuid, input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get a product by uuid")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid uuid",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            )
    })
    ResponseEntity<ProductDTO> getOne(@PathVariable("uuid") String uuid) {
        UUID parsedUuid = ValidationUtils.parseUUID(uuid);
        Optional<Product> product = findUseCase.findById(parsedUuid);
        ProductDTO productDTO = productDTOMapper.domainToDTO(product.orElseThrow(ProductNotFoundException::new));
        return ResponseEntity.status(201).body(productDTO);
    }

    @GetMapping
    @Operation(summary = "Get a products by fantasyName")
    @Parameter(
            in = ParameterIn.QUERY,
            name ="q",
            schema = @Schema(type = "string"),
            description = "filter by fantasyName"
    )
    ResponseEntity<List<ProductDTO>> getProducts(@Parameter(hidden = true) @RequestParam("q") String fantasyName) {
        if (fantasyName.isEmpty()) {
            throw new ParameterNullException(fantasyName);
        }
        List<Product> products = findUseCase.filterByFantasyName(fantasyName);
        List<ProductDTO> productDTOS = products.stream()
                .map(productDTOMapper::domainToDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(productDTOS);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete a product by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid uuid",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
    })
    ResponseEntity<Void> deleteOne(@PathVariable("uuid") String uuid) {
        UUID parsedUuid = ValidationUtils.parseUUID(uuid);
        deleteUseCase.deleteById(parsedUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
