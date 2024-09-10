package com.javastudio.grandmafood.features.core.controllers.order;

import com.javastudio.grandmafood.common.web.ApiError;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderDTO;
import com.javastudio.grandmafood.features.core.controllers.order.dto.OrderDTOMapper;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderInfo;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderCreateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping(path = "/api/v1/orders")
@RestController
@Tag(name = "orders")
public class OrderController {

    private final OrderCreateUseCase orderCreateUseCase;

    private final OrderDTOMapper orderDTOMapper;

    public OrderController(OrderCreateUseCase orderCreateUseCase, OrderDTOMapper orderDTOMapper) {
        this.orderCreateUseCase = orderCreateUseCase;
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
                    description = "Product not found",
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
    ResponseEntity<OrderDTO> create(@RequestBody OrderCreateDTO orderCreateDTO) {
        OrderCreateInput input = orderDTOMapper.dtoToDomain(orderCreateDTO);
        Order order = orderCreateUseCase.create(input);
        OrderInfo orderInfo = new OrderInfo(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        OrderDTO orderDTO = orderDTOMapper.domainToDTO(order, orderInfo);
        return ResponseEntity.status(201).body(orderDTO);
    }

}
