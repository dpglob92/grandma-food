package com.javastudio.grandmafood.core.integration.sales;

import com.javastudio.grandmafood.features.core.entities.sales.SalesReport;
import com.javastudio.grandmafood.features.core.usecases.sales.SalesReportUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SalesReportIntegrationTests {

    @Autowired
    private SalesReportUseCase salesReportUseCase;

    private final UUID product1 = UUID.fromString("02184924-1d2a-4006-bbb2-31fe08ad46c1");

    private final UUID product2 = UUID.fromString("9c53676f-d4dd-4cca-b69f-8203cf70d474");

    private final UUID product3 = UUID.fromString("5421030d-04db-46ab-b5f2-2429c6b2d0d9");


    private void assertProductInReport(
            List<SalesReport.ProductGrossUnitReport> productGrossUnitReports,
            UUID productUuid,
            BigDecimal grossProfit,
            int unitsSold
    ){
        Optional<SalesReport.ProductGrossUnitReport> container = productGrossUnitReports.stream()
                .filter(r -> r.getUuid().equals(productUuid))
                .findFirst();

        if (container.isPresent()) {
            Assertions.assertThat(container.get().getGrossProfit()).isEqualByComparingTo(grossProfit);
            Assertions.assertThat(container.get().getUnitsSold()).isEqualByComparingTo(unitsSold);
        } else{
            Assertions.fail(String.format("product with id %s not found in report", productUuid));
        }
    }

    @Test
    @Sql({"classpath:setup-data/sales/sales-base-1.sql"})
    public void Should_GetSalesReport_WhenYearIs2022() {

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20220101", "20221231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(3);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);

        Assertions.assertThat(salesReport.getBestSellingProducts().getFirst().getUuid()).isEqualTo(product3);
        Assertions.assertThat(salesReport.getWorstSellingProducts().getFirst().getUuid()).isEqualTo(product1);

        List<SalesReport.ProductGrossUnitReport> productGrossUnitReports = salesReport.getProductReport();

        assertProductInReport(productGrossUnitReports, product1, new BigDecimal("76.5"), 3);
        assertProductInReport(productGrossUnitReports, product2, new BigDecimal("75"), 4);
        assertProductInReport(productGrossUnitReports, product3, new BigDecimal("68.8"), 8);
    }

    @Test
    @Sql({"classpath:setup-data/sales/sales-base-1.sql"})
    public void Should_GetSalesReport_WhenUsingQ12022() {

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20220101", "20220431");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(2);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);

        Assertions.assertThat(salesReport.getBestSellingProducts().getFirst().getUuid()).isEqualTo(product2);
        Assertions.assertThat(salesReport.getWorstSellingProducts().getFirst().getUuid()).isEqualTo(product1);

        List<SalesReport.ProductGrossUnitReport> productGrossUnitReports = salesReport.getProductReport();

        assertProductInReport(productGrossUnitReports, product1, new BigDecimal("51"), 2);
        assertProductInReport(productGrossUnitReports, product2, new BigDecimal("75"), 4);
    }


}
