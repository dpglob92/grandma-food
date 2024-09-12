package com.javastudio.grandmafood.features.core.controllers.product;

import com.javastudio.grandmafood.features.core.controllers.product.dto.SalesReportDTO;
import com.javastudio.grandmafood.features.core.entities.sales.SalesReport;

import java.util.List;

public class SalesReportDTOMapper {

    public static SalesReportDTO salesReportToDto(SalesReport report) {
        // Map ProductGrossUnitReport to ProductGrossUnitReportDTO
        List<SalesReportDTO.ProductGrossUnitReportDTO> productReportDTO = report.getProductReport().stream()
                .map(product -> SalesReportDTO.ProductGrossUnitReportDTO.builder()
                        .uuid(product.getUuid())
                        .fantasyName(product.getName())
                        .grossProfit(product.getGrossProfit())
                        .unitsSold(product.getUnitsSold())
                        .build())
                .toList();

        // Map ProductGrossUnitReport to ProductUnitReportDTO for best-selling products
        List<SalesReportDTO.ProductUnitReportDTO> bestSellingProductsDTO = report.getBestSellingProducts().stream()
                .map(product -> SalesReportDTO.ProductUnitReportDTO.builder()
                        .uuid(product.getUuid())
                        .fantasyName(product.getName())
                        .unitsSold(product.getUnitsSold())
                        .build())
                .toList();

        // Map ProductGrossUnitReport to ProductUnitReportDTO for worst-selling products
        List<SalesReportDTO.ProductUnitReportDTO> worstSellingProductsDTO = report.getWorstSellingProducts().stream()
                .map(product -> SalesReportDTO.ProductUnitReportDTO.builder()
                        .uuid(product.getUuid())
                        .fantasyName(product.getName())
                        .unitsSold(product.getUnitsSold())
                        .build())
                .toList();

        // Build the SalesReportDTO
        return SalesReportDTO.builder()
                .productReport(productReportDTO)
                .bestSellingProducts(bestSellingProductsDTO)
                .worstSellingProducts(worstSellingProductsDTO)
                .build();
    }
}
