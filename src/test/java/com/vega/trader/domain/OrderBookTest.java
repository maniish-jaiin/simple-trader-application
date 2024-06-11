package com.vega.trader.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.vega.trader.domain.Order.OrderType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderBookTest {
    private OrderBook orderBook;

    @BeforeEach
    public void setup() {
        orderBook = new OrderBook();
    }

    @Test
    public void testAddOrder() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder = new Order("O1", "T1", OrderType.BUY, Optional.of(BigDecimal.valueOf(100)), 10, instrument);
        Order sellOrder = new Order("O2", "T2", OrderType.SELL, Optional.of(BigDecimal.valueOf(100)), 10, instrument);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);

        assertEquals(1, orderBook.getBuyOrders().size());
        assertEquals(1, orderBook.getSellOrders().size());

        assertTrue(orderBook.getBuyOrders().contains(buyOrder));
        assertTrue(orderBook.getSellOrders().contains(sellOrder));
    }

    @Test
    public void testCancelOrder() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder = new Order("O1", "T1", OrderType.BUY, Optional.of(BigDecimal.valueOf(100)), 10, instrument);
        Order sellOrder = new Order("O2", "T2", OrderType.SELL, Optional.empty(), 10, instrument);

        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);
        orderBook.cancelOrder(buyOrder);
        orderBook.cancelOrder(sellOrder);

        assertEquals(0, orderBook.getBuyOrders().size());
        assertEquals(0, orderBook.getSellOrders().size());
    }

    @Test
    public void testMatchOrdersWhenOrdersMatch() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder1 = new Order("O1", "T1", OrderType.BUY, Optional.empty(), 10, instrument);
        Order buyOrder2 = new Order("O2", "T2", OrderType.BUY, Optional.empty(), 10, instrument);
        Order sellOrder1 = new Order("O3", "T3", OrderType.SELL, Optional.empty(), 10, instrument);
        Order sellOrder2 = new Order("O4", "T4", OrderType.SELL, Optional.empty(), 10, instrument);

        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.addOrder(sellOrder2);

        List<Order> matchedOrders = orderBook.matchOrders();

        assertEquals(4, matchedOrders.size());
        assertTrue(matchedOrders.contains(buyOrder1));
        assertTrue(matchedOrders.contains(sellOrder1));
    }

    @Test
    public void testMatchOrdersWhenOrdersDoNotMatch() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder1 = new Order("O1", "T1", OrderType.BUY, Optional.empty(), 10, instrument);
        Order buyOrder2 = new Order("O2", "T2", OrderType.BUY, Optional.empty(), 11, instrument);
        Order sellOrder1 = new Order("O3", "T3", OrderType.SELL, Optional.empty(), 12, instrument);
        Order sellOrder2 = new Order("O4", "T4", OrderType.SELL, Optional.empty(), 13, instrument);

        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.addOrder(sellOrder2);

        List<Order> matchedOrders = orderBook.matchOrders();

        assertEquals(0, matchedOrders.size());
        assertTrue(matchedOrders.isEmpty());
    }

    @Test
    public void testMatchOrdersWhenOrdersMatchWithCancel() {
        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI1", "CFI1", BigDecimal.valueOf(100), List.of(
                new FinancialInstrument("FI1", "AAPL", BigDecimal.valueOf(150)),
                new FinancialInstrument("FI2", "GOOG", BigDecimal.valueOf(200))
        ));
        Order buyOrder1 = new Order("O1", "T1", OrderType.BUY, Optional.empty(), 10, instrument);
        Order buyOrder2 = new Order("O2", "T2", OrderType.BUY, Optional.empty(), 10, instrument);
        Order sellOrder1 = new Order("O3", "T3", OrderType.SELL, Optional.empty(), 10, instrument);
        Order sellOrder2 = new Order("O4", "T4", OrderType.SELL, Optional.empty(), 10, instrument);

        orderBook.addOrder(buyOrder1);
        orderBook.addOrder(buyOrder2);
        orderBook.addOrder(sellOrder1);
        orderBook.cancelOrder(buyOrder1);
        orderBook.addOrder(sellOrder2);

        List<Order> matchedOrders = orderBook.matchOrders();

        assertEquals(2, matchedOrders.size());
    }
}