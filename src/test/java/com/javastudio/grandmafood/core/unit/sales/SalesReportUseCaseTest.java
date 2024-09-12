package com.javastudio.grandmafood.core.unit.sales;

import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.features.core.database.projections.ProductReportView;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.sales.SalesReport;
import com.javastudio.grandmafood.features.core.usecases.sales.SalesReportUseCase;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesReportUseCaseTest {

    @Mock
    private OrderItemJPAEntityRepository repository;

    private SalesReportUseCase salesReportUseCase;

    private void assertInvalidDateInput(ThrowableAssert.ThrowingCallable callMethod, String fieldName){
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                callMethod,
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo(fieldName);
    }

    @BeforeEach
    public void setup() {
        salesReportUseCase = new SalesReportUseCase(repository);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenStartDateIsNull() {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport(null, "20230101"),
                "startDate"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEndDateIsNull() {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport("20230101", null),
                "endDate"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "hi",
            "x-02-12",
            "105066",
            "100001530",
            "20221510",
            "20230945",
    })
    public void Should_ThrowInvalidInputException_WhenStartDateIsInvalid(String input) {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport(input, "20230101"),
                "startDate"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "hi",
            "x-02-12",
            "105066",
            "100001530",
            "20221510",
            "20230945",
    })
    public void Should_ThrowInvalidInputException_WhenEndDateIsInvalid(String input) {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport("20230101", input),
                "endDate"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenStartDateAndEndDateAreTheSame() {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport("20230101", "20230101"),
                "startDate/endDate"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEndDateIsBeforeStartDate() {
        assertInvalidDateInput(
                () -> salesReportUseCase.computeSaleReport("20230101", "20220501"),
                "startDate/endDate"
        );
    }

    @Test
    public void Should_ReturnSizeOfListsOf_0_0_0_WhenZeroProductsAreInTheReport() {

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(List.of());

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(0);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(0);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(0);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_1_1_1_WhenOneProductIsInTheReport() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(1);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_2_2_2_WhenTwoProductsAreInTheReportWithTheSameUnitsSold() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);
        ProductReportView p2 = getDummyProductReportView(UUID.randomUUID(), "B", 10, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1, p2)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(2);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(2);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(2);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_2_1_1_WhenTwoProductsAreInTheReportWithDifferentUnitsSold() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);
        ProductReportView p2 = getDummyProductReportView(UUID.randomUUID(), "B", 20, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1, p2)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(2);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_3_1_1_WhenThreeProductsAreInTheReportWithDifferentUnitsSold() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);
        ProductReportView p2 = getDummyProductReportView(UUID.randomUUID(), "B", 20, BigDecimal.ZERO);
        ProductReportView p3 = getDummyProductReportView(UUID.randomUUID(), "C", 25, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1, p2, p3)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(3);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_3_2_1_WhenThreeProductsAreInTheReportWithTheFirstTwoHavingTheSameUnitsSold() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);
        ProductReportView p2 = getDummyProductReportView(UUID.randomUUID(), "B", 10, BigDecimal.ZERO);
        ProductReportView p3 = getDummyProductReportView(UUID.randomUUID(), "C", 30, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1, p2, p3)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(3);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(2);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(1);
    }

    @Test
    public void Should_ReturnSizeOfListsOf_3_1_2_WhenThreeProductsAreInTheReportWithTheLastTwoHavingTheSameUnitsSold() {
        ProductReportView p1 = getDummyProductReportView(UUID.randomUUID(), "A", 10, BigDecimal.ZERO);
        ProductReportView p2 = getDummyProductReportView(UUID.randomUUID(), "B", 20, BigDecimal.ZERO);
        ProductReportView p3 = getDummyProductReportView(UUID.randomUUID(), "C", 20, BigDecimal.ZERO);

        when(repository.getSaleReport(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(
                List.of(p1, p2, p3)
        );

        SalesReport salesReport = salesReportUseCase.computeSaleReport("20230101", "20231231");

        Assertions.assertThat(salesReport.getProductReport()).hasSize(3);
        Assertions.assertThat(salesReport.getBestSellingProducts()).hasSize(1);
        Assertions.assertThat(salesReport.getWorstSellingProducts()).hasSize(2);
    }

    private ProductReportView getDummyProductReportView(
            UUID uuid,
            String name,
            int unitsSold,
            BigDecimal grossProfit
    ) {
        return new ProductReportView() {
            @Override
            public String getProductName() {
                return name;
            }

            @Override
            public UUID getProductId() {
                return uuid;
            }

            @Override
            public Integer getUnitsSold() {
                return unitsSold;
            }

            @Override
            public BigDecimal getGrossProfit() {
                return grossProfit;
            }
        };
    }

}
