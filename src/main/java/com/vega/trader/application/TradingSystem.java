package com.vega.trader.application;

import com.vega.trader.domain.Order;
import com.vega.trader.domain.OrderBook;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TradingSystem {
    private final OrderBook orderBook;

    public void addOrder(Order order) {
        orderBook.addOrder(order);
    }

    public void cancelOrder(Order order) {
        orderBook.cancelOrder(order);
    }

    public void executeTrades() {
        var matchedOrders = orderBook.matchOrders();
        matchedOrders.forEach(order -> System.out.println("Trade executed: " + order));
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}