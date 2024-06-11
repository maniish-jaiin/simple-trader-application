package com.vega.trader.domain;

import java.math.BigDecimal;

public record FinancialInstrument(
        String id,
        String symbol,
        BigDecimal currentMarketPrice
) {
}