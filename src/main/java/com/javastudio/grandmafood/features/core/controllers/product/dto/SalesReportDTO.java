package com.javastudio.grandmafood.features.core.controllers.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class SalesReportDTO {

    private List<ProductGrossUnitReportDTO> productReport;

    private List<ProductUnitReportDTO> bestSellingProducts;

    private List<ProductUnitReportDTO> worstSellingProducts;

    @Data
    @AllArgsConstructor
    @Builder
    public static class ProductGrossUnitReportDTO {

        private UUID uuid;

        private String fantasyName;

        private BigDecimal grossProfit;

        private Integer unitsSold;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ProductUnitReportDTO {

        private UUID uuid;

        private String fantasyName;

        private Integer unitsSold;
    }
}
