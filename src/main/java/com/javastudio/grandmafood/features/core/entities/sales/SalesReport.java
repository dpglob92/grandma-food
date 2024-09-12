package com.javastudio.grandmafood.features.core.entities.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Represents a sales report containing information about product sales.
 */
@Data
@AllArgsConstructor
@Builder
public class SalesReport {

    /**
     * The list of product with profit gross and total units sold
     */
    private List<ProductGrossUnitReport> productReport;

    /**
     * The list of best-selling products based on units sold.
     */
    private List<ProductGrossUnitReport> bestSellingProducts;

    /**
     * The list of worst-selling products based on units sold.
     */
    private List<ProductGrossUnitReport> worstSellingProducts;

    /**
     * Represents a product's profit gross and units sold report.
     */
    @Data
    @AllArgsConstructor
    @Builder
    public static class ProductGrossUnitReport {

        /**
         * The UUID of the product.
         */
        private UUID uuid;

        /**
         * The name of the product.
         */
        private String name;

        /**
         * The gross profit of the product.
         */
        private BigDecimal grossProfit;

        /**
         * The number of units sold for the product.
         */
        private Integer unitsSold;
    }
}
