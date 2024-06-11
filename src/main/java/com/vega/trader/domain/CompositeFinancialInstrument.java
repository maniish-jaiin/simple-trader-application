package com.vega.trader.domain;

import java.math.BigDecimal;
import java.util.List;

public record CompositeFinancialInstrument(
        String id,
        String symbol,
        BigDecimal currentMarketPrice,
        List<FinancialInstrument> financialInstruments
) {
    public CompositeFinancialInstrument {
        if (financialInstruments.isEmpty() || financialInstruments.size() > 3) {
            throw new IllegalArgumentException("A composite financial instrument must have 1 to 3 financialInstruments.");
        }
    }
}