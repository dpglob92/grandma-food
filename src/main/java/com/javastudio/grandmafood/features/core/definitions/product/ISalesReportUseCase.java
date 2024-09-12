package com.javastudio.grandmafood.features.core.definitions.product;

import com.javastudio.grandmafood.features.core.entities.sales.SalesReport;

public interface ISalesReportUseCase {

    SalesReport computeSaleReport(String startDate, String endDate);
}
