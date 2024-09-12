package com.javastudio.grandmafood.features.core.controllers.order;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderDTO;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderDTOMapper;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderInfo;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderUpdateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RequestMapping(path = "/api/v1/orders")
@RestController
@Tag(name = "orders")
public class OrderController {

    private final OrderCreateUseCase orderCreateUseCase;

    private final OrderUpdateUseCase orderUpdateUseCase;

    private final OrderDTOMapper orderDTOMapper;

    public OrderController(
            OrderCreateUseCase orderCreateUseCase,
            OrderUpdateUseCase orderUpdateUseCase,
            OrderDTOMapper orderDTOMapper
    ) {
        this.orderCreateUseCase = orderCreateUseCase;
        this.orderUpdateUseCase = orderUpdateUseCase;
        this.orderDTOMapper = orderDTOMapper;
    }

    @PostMapping("/")
    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new order",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))
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
                    responseCode = "404",
                    description = "Product not found \n Client not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
    })
    ResponseEntity<OrderDTO> create(@RequestBody OrderCreateDTO orderCreateDTO) {
        OrderCreateInput input = orderDTOMapper.dtoToDomain(orderCreateDTO);
        Order order = orderCreateUseCase.create(input);
        OrderInfo orderInfo = new OrderInfo(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        OrderDTO orderDTO = orderDTOMapper.domainToDTO(order, orderInfo);
        return ResponseEntity.status(201).body(orderDTO);
    }

    @PatchMapping("/{uuid}/delivered/{timestamp}")
    @Operation(summary = "update order delivery date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))
            }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid uuid or date",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }
            ),
    })
    ResponseEntity<OrderDTO> update(@PathVariable("uuid") String uuid, @PathVariable("timestamp") String timestamp) {
        UUID parsedUuid = ValidationUtils.parseUUID(uuid);
        Order order = orderUpdateUseCase.updateDeliveryDate(parsedUuid, timestamp);
        OrderInfo orderInfo = new OrderInfo(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        OrderDTO orderDTO = orderDTOMapper.domainToDTO(order, orderInfo);
        return ResponseEntity.status(200).body(orderDTO);
    }

}
