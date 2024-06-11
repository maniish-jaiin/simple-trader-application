# Simplified Trading System

## Project Overview

This project implements a simplified trading system. The system handles buy and sell orders for composite financial instruments (e.g., a basket of two underlying stocks). The key functionalities include adding orders, canceling orders, and executing trades based on the best available market prices.

## Technologies Used

- **Java 21**: The latest version of Java is used to leverage new features and enhancements.
- **Spring Boot**: A framework to build and run the RESTful web service.
- **JUnit 5**: Used for unit testing.
- **Mockito**: Used for mocking dependencies in unit tests.
- **Gradle**: Build automation tool.

## Project Structure

- **domain**: Contains the domain models and the `OrderBook` class.
- **application**: Contains the `TradingSystem` class, which is the core service for managing orders.
- **presentation**: Contains the `TradingSystemController` class, which exposes RESTful endpoints to interact with the trading system.
- **test**: Contains unit tests for the domain and service classes.

## Key Classes

### Domain Models

- **FinancialInstrument**: Represents a basic financial instrument with an ID, symbol, and current market price.
- **CompositeFinancialInstrument**: Represents a composite financial instrument consisting of 1 to 3 financial instruments.
- **Order**: Represents a buy or sell order for a composite financial instrument.

### OrderBook

- Manages buy and sell orders.
- Provides methods to add, cancel, and match orders.

### TradingSystemService

- A service class that interacts with the `OrderBook`.
- Methods include `addOrder`, `cancelOrder`, and `executeTrades`.

### TradingSystemController

- Exposes RESTful endpoints to interact with the trading system.
- Endpoints:
    - `POST /api/trading/order`: Add a new order.
    - `DELETE /api/trading/order/{orderId}`: Cancel an order.
    - `POST /api/trading/execute`: Execute trades based on the current orders in the order book.

### Building and Running

1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Gradle:

   ```sh
   ./gradlew clean build

### Example

Adding a BUY order

```shell
curl -X POST "http://localhost:8080/api/trading/order" 
-d "orderId=O1&traderId=T1&type=BUY&price=100&quantity=10&componentIds=FI1,FI2&componentSymbols=AAPL,GOOG&componentPrices=150,200"
```

Adding a SELL order
```shell
curl -X POST "http://localhost:8080/api/trading/order" \                                                                       ░▒▓ ✔  22:29:32 ▓▒░
-d "orderId=O1&traderId=T1&type=SELL&price=100&quantity=10&componentIds=FI1,FI2&componentSymbols=AAPL,GOOG&componentPrices=150,200"
```

Executing orders
```shell
curl -X POST "http://localhost:8080/api/trading/execute"
```

Console output
```
Trade executed: Order[orderId=O1, traderId=T1, type=BUY, price=Optional[100], quantity=10, instrument=CompositeFinancialInstrument[id=CFI, symbol=Composite, currentMarketPrice=100, financialInstruments=[FinancialInstrument[id=FI1, symbol=AAPL, currentMarketPrice=150], FinancialInstrument[id=FI2, symbol=GOOG, currentMarketPrice=200]]]]
```