package com.vega.trader.domain;

import com.vega.trader.application.TradingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.vega.trader.domain.Order.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class TradingSystemTest {

    private OrderBook orderBook;
    private TradingSystem tradingSystem;

    @BeforeEach
    public void setup() {
        orderBook = Mockito.mock(OrderBook.class);
        tradingSystem = new TradingSystem(orderBook);
    }
    @Test
    public void testAddAndCancelOrder() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder = new Order("O1", "T1", OrderType.BUY, Optional.of(BigDecimal.valueOf(100)), 10, instrument);
        Order sellOrder = new Order("O2", "T2", OrderType.SELL, Optional.of(BigDecimal.valueOf(100)), 10, instrument);

        // Test adding orders
        tradingSystem.addOrder(buyOrder);
        tradingSystem.addOrder(sellOrder);

        // Verify that orders are added to the order book
        verify(orderBook).addOrder(buyOrder);
        verify(orderBook).addOrder(sellOrder);

        // Test canceling orders
        tradingSystem.cancelOrder(buyOrder);
        tradingSystem.cancelOrder(sellOrder);

        // Verify that orders are canceled in the order book
        verify(orderBook).cancelOrder(buyOrder);
        verify(orderBook).cancelOrder(sellOrder);
    }

    @Test
    public void testExecuteTrades() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument(
                "CFI1",
                "CFI1",
                BigDecimal.valueOf(100),
                List.of(
                        new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                        new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
                ));
        Order buyOrder = new Order("O1", "T1", OrderType.BUY, Optional.of(BigDecimal.valueOf(100)), 10, instrument);
        Order sellOrder = new Order("O2", "T2", OrderType.SELL, Optional.of(BigDecimal.valueOf(100)), 10, instrument);

        tradingSystem.addOrder(buyOrder);
        tradingSystem.addOrder(sellOrder);
        tradingSystem.executeTrades();

        // Verify that matchOrders was called
        verify(orderBook).matchOrders();
    }

    @Test
    public void testCompositeFinancialInstrumentValidation() {
        FinancialInstrument fi1 = new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150));
        FinancialInstrument fi2 = new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200));
        FinancialInstrument fi3 = new FinancialInstrument("FI3", "AMZN", BigDecimal.valueOf(100));

        assertDoesNotThrow(() -> new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(fi1, fi2)));
        assertDoesNotThrow(() -> new CompositeFinancialInstrument("CFI2", "CFI2", BigDecimal.valueOf(200), List.of(fi1, fi2, fi3)));
    }

    @Test
    public void testCompositeFinancialInstrumentInvalid() {
        FinancialInstrument fi1 = new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150));
        FinancialInstrument fi2 = new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200));
        FinancialInstrument fi3 = new FinancialInstrument("FI3", "AMZN", BigDecimal.valueOf(100));
        FinancialInstrument fi4 = new FinancialInstrument("FI4", "MSFT", BigDecimal.valueOf(300));

        assertThrows(IllegalArgumentException.class, () -> new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(fi1, fi2, fi3, fi4)));
    }
}