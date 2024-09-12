package com.javastudio.grandmafood.features.core.usecases.sales;

import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.projections.ProductReportView;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.ISalesReportUseCase;
import com.javastudio.grandmafood.features.core.entities.sales.SalesReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SalesReportUseCase implements ISalesReportUseCase {

    private final OrderItemJPAEntityRepository repository;

    Logger logger = LoggerFactory.getLogger(SalesReportUseCase.class);

    public SalesReportUseCase(OrderItemJPAEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public SalesReport computeSaleReport(String startDate, String endDate) {
        logger.info("Validating and parsing start and end date");
        LocalDate parsedStartDate = ValidationUtils.parseYYYMMDDToLocalDate(startDate, "startDate");
        LocalDate parsedEndDate = ValidationUtils.parseYYYMMDDToLocalDate(endDate, "endDate");

        if (parsedStartDate.equals(parsedEndDate)) {
            InvalidInputException ex = new InvalidInputException();
            ex.addError("startDate/endDate", "startDate cannot be the same as endDate");
            throw ex;
        }

        ValidationUtils.assertEndDateAfterStartDate(parsedStartDate, parsedEndDate, "startDate/endDate");

        logger.info("Executing query for generating sales report");
        System.out.println(parsedStartDate.atStartOfDay());
        System.out.println(parsedEndDate.atTime(23, 59, 59));
        List<ProductReportView> generalProductReport = repository.getSaleReport(
                parsedStartDate.atStartOfDay(),
                parsedEndDate.atTime(23, 59, 59)
        );

        if (generalProductReport.isEmpty()){
            return SalesReport.builder()
                    .productReport(List.of())
                    .bestSellingProducts(List.of())
                    .worstSellingProducts(List.of())
                    .build();
        }

        List<SalesReport.ProductGrossUnitReport> productReport = generalProductReport.stream()
                .map(gr ->
                        SalesReport.ProductGrossUnitReport.builder()
                                .uuid(gr.getProductId())
                                .name(gr.getProductName())
                                .unitsSold(gr.getUnitsSold())
                                .grossProfit(gr.getGrossProfit())
                                .build()
                )
                .toList();

        SalesReport.ProductGrossUnitReport bestSellingByUnitsSold = productReport.getFirst();
        SalesReport.ProductGrossUnitReport worstSellingByUnitsSold = productReport.getLast();

        List<SalesReport.ProductGrossUnitReport> bestSellingProductsByUnitsSold = new ArrayList<>();
        bestSellingProductsByUnitsSold.add(bestSellingByUnitsSold);

        List<SalesReport.ProductGrossUnitReport> worstSellingProductsByUnitsSold = new ArrayList<>();
        worstSellingProductsByUnitsSold.add(worstSellingByUnitsSold);

        // Checking for other products with the same units sold as the top product
        int i = 1;
        while (i < productReport.size()
                && Objects.equals(productReport.get(i).getUnitsSold(), bestSellingByUnitsSold.getUnitsSold())
        ) {
            bestSellingProductsByUnitsSold.add(productReport.get(i));
            i++;
        }

        // Checking for other products with the same units sold as the bottom product
        int j = productReport.size() - 2;
        while (j >= 0
                && Objects.equals(productReport.get(j).getUnitsSold(), worstSellingByUnitsSold.getUnitsSold())) {
            worstSellingProductsByUnitsSold.add(productReport.get(j));
            j--;
        }

        return SalesReport.builder()
                .productReport(productReport)
                .bestSellingProducts(bestSellingProductsByUnitsSold)
                .worstSellingProducts(worstSellingProductsByUnitsSold)
                .build();
    }
}
