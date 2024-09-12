package com.javastudio.grandmafood.features.core.database.projections;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductReportView {

    String getProductName();

    UUID getProductId();

    Integer getUnitsSold();

    BigDecimal getGrossProfit();
}
