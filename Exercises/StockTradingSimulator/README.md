# Stock Trading Simulator

A JavaFX desktop application that simulates stock trading with real-time stock quotes.

## Features

- Real-time stock quotes using Alpha Vantage API
- Portfolio management (buy/sell stocks)
- Watchlist functionality
- Automatic price updates
- Persistent data storage (JSON)
- Modern JavaFX UI

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Alpha Vantage API key (free tier available)

## Setup

1. Clone this repository
2. Get your free API key from [Alpha Vantage](https://www.alphavantage.co/support/#api-key)
3. Update the API key in `src/main/java/com/example/stockapp/StockQuoteService.java`:
   ```java
   private static final String API_KEY = "YOUR_API_KEY";
   ```

## Building and Running

To build and run the application, use the following Maven commands:

```bash
mvn javafx:run
```

## Usage

### Portfolio Management

- **Buy Stocks**: Enter the stock symbol, number of shares, and price, then click "Buy"
- **Sell Stocks**: Enter the stock symbol, number of shares, and price, then click "Sell"
- **View Holdings**: See your current stock holdings in the "Holdings" tab
- **Track Performance**: Monitor your portfolio's total value and profit/loss

### Watchlist

- **Add to Watchlist**: Enter a stock symbol and click "Add to Watchlist"
- **Remove from Watchlist**: Enter a stock symbol and click "Remove from Watchlist"
- **View Watchlist**: See your watchlist stocks in the "Watchlist" tab
- **Quick Buy**: Select a stock from your watchlist and click "Buy Selected"

## Project Structure

```
src/
  main/
    java/
      com/
        example/
          stockapp/
            StockTradingApp.java (Main application)
            StockQuoteService.java (API service)
            Portfolio.java (Portfolio model)
            PortfolioManager.java (Data persistence)
            Holding.java (Stock holding model)
            StockQuote.java (Stock quote model)
pom.xml
README.md
```

## Notes

- The Alpha Vantage free API has rate limits (5 calls per minute, 100 per day)
- Stock prices are updated every 30 seconds
- Portfolio data is saved to `portfolio.json` in the project root directory 