package com.vega.trader.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Getter
public class OrderBook {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public void addOrder(Order order) {
        switch (order.type()) {
            case BUY -> buyOrders.add(order);
            case SELL -> sellOrders.add(order);
        }
    }

    public void cancelOrder(Order order) {
        switch (order.type()) {
            case BUY -> buyOrders.remove(order);
            case SELL -> sellOrders.remove(order);
        }
    }

    public List<Order> matchOrders() {
        buyOrders.sort(Comparator.comparing(Order::quantity));
        sellOrders.sort(Comparator.comparing(Order::quantity));

        List<Order> matchedOrders = new ArrayList<>();

        for (Order buyOrder : buyOrders) {
            for (Order sellOrder : sellOrders) {
                if (buyOrder.quantity() == sellOrder.quantity()) {
                    matchedOrders.add(buyOrder);
                    matchedOrders.add(sellOrder);
                    break;
                }
            }
        }

        buyOrders.removeAll(matchedOrders);
        sellOrders.removeAll(matchedOrders);

        return matchedOrders;
    }
}