package com.javastudio.grandmafood.features.core.controllers.product;

import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductDTO;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductDTOMapper {

    public ProductDTO domainToDTO(Product product){
        return ProductDTO.builder()
                .uuid(product.getUuid())
                .fantasyName(product.getName())
                .description(product.getDescription())
                .foodCategory(product.getFoodCategory())
                .price(product.getPrice().toString())
                .available(product.isAvailable())
                .build();
    }

    public ProductCreateInput dtoToDomain(ProductCreateDTO productCreateDTO){

        BigDecimal parsedPrice;

        try {
            parsedPrice = new BigDecimal(productCreateDTO.getPrice());
        } catch (NumberFormatException | NullPointerException e) {
            InvalidInputException ex = new InvalidInputException();
            ex.addError("price", "invalid format for price");
            throw ex;
        }

        return ProductCreateInput.builder()
                .name(productCreateDTO.getFantasyName())
                .description(productCreateDTO.getDescription())
                .foodCategory(productCreateDTO.getFoodCategory())
                .price(parsedPrice)
                .available(productCreateDTO.isAvailable())
                .build();
    }
}
