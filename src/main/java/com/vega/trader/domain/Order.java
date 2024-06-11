package com.vega.trader.domain;

import java.math.BigDecimal;
import java.util.Optional;

public record Order(
        String orderId,
        String traderId,
        OrderType type,
        Optional<BigDecimal> price,
        int quantity,
        CompositeFinancialInstrument instrument
) {
    public enum OrderType {
        BUY, SELL
    }
}