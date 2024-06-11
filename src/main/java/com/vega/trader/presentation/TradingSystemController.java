package com.vega.trader.presentation;

import com.vega.trader.application.TradingSystem;
import com.vega.trader.domain.CompositeFinancialInstrument;
import com.vega.trader.domain.FinancialInstrument;
import com.vega.trader.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/trading")
public class TradingSystemController {

    private final TradingSystem tradingSystemService;

    public TradingSystemController(TradingSystem tradingSystemService) {
        this.tradingSystemService = tradingSystemService;
    }

    @PostMapping("/order")
    public ResponseEntity<String> addOrder(@RequestParam String orderId,
                                           @RequestParam String traderId,
                                           @RequestParam Order.OrderType type,
                                           @RequestParam BigDecimal price,
                                           @RequestParam int quantity,
                                           @RequestParam List<String> componentIds,
                                           @RequestParam List<String> componentSymbols,
                                           @RequestParam List<BigDecimal> componentPrices) {

        List<FinancialInstrument> components = IntStream.range(0, componentIds.size())
                .mapToObj(i -> new FinancialInstrument(componentIds.get(i), componentSymbols.get(i), componentPrices.get(i)))
                .collect(Collectors.toList());

        CompositeFinancialInstrument instrument = new CompositeFinancialInstrument("CFI", "Composite", BigDecimal.valueOf(100), components);
        Order order = new Order(orderId, traderId, type, Optional.of(price), quantity, instrument);
        tradingSystemService.addOrder(order);

        return ResponseEntity.ok("Order added successfully");
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        Order order = tradingSystemService.getOrderBook().getBuyOrders().stream()
                .filter(o -> o.orderId().equals(orderId))
                .findFirst()
                .orElse(tradingSystemService.getOrderBook().getSellOrders().stream()
                        .filter(o -> o.orderId().equals(orderId))
                        .findFirst()
                        .orElse(null));

        if (order != null) {
            tradingSystemService.cancelOrder(order);
            return ResponseEntity.ok("Order cancelled successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeTrades() {
        tradingSystemService.executeTrades();
        return ResponseEntity.ok("Trades executed successfully");
    }
}