package com.example.stockapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockTradingApp extends Application {

    private Portfolio portfolio;
    private StockQuoteService quoteService;
    private ScheduledExecutorService scheduler;
    
    // UI Components
    private Label cashBalanceLabel;
    private Label totalValueLabel;
    private Label profitLossLabel;
    private TableView<Holding> holdingsTable;
    private TableView<StockQuote> watchlistTable;
    private TextField symbolField;
    private TextField sharesField;
    private TextField priceField;
    private TextField watchlistSymbolField;
    
    // Observable lists for tables
    private ObservableList<Holding> holdingsList;
    private ObservableList<StockQuote> watchlistQuotes;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize services and data
        portfolio = PortfolioManager.loadPortfolio();
        quoteService = new StockQuoteService();
        holdingsList = FXCollections.observableArrayList(portfolio.getHoldings());
        watchlistQuotes = FXCollections.observableArrayList();
        
        // Create UI
        BorderPane root = new BorderPane();
        
        // Top section with portfolio summary
        VBox topSection = createTopSection();
        root.setTop(topSection);
        
        // Center section with holdings and watchlist
        TabPane centerSection = createCenterSection();
        root.setCenter(centerSection);
        
        // Bottom section with trading controls
        VBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);
        
        // Create scene and stage
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Stock Trading Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Start background tasks
        startBackgroundTasks();
        
        // Update UI with initial data
        updatePortfolioSummary();
        updateWatchlist();
    }
    
    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(15));
        topSection.setStyle("-fx-background-color: #f0f0f0;");
        
        Label titleLabel = new Label("Stock Trading Simulator");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Portfolio summary grid
        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(10);
        summaryGrid.setAlignment(Pos.CENTER);
        
        cashBalanceLabel = new Label("Cash: $0.00");
        totalValueLabel = new Label("Total Value: $0.00");
        profitLossLabel = new Label("P/L: $0.00");
        
        cashBalanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalValueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        profitLossLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        summaryGrid.add(cashBalanceLabel, 0, 0);
        summaryGrid.add(totalValueLabel, 1, 0);
        summaryGrid.add(profitLossLabel, 2, 0);
        
        topSection.getChildren().addAll(titleLabel, summaryGrid);
        
        return topSection;
    }
    
    private TabPane createCenterSection() {
        TabPane tabPane = new TabPane();
        
        // Holdings Tab
        Tab holdingsTab = new Tab("Holdings");
        holdingsTab.setClosable(false);
        
        VBox holdingsContent = new VBox(10);
        holdingsContent.setPadding(new Insets(15));
        
        holdingsTable = new TableView<>();
        holdingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Holding, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        
        TableColumn<Holding, Integer> sharesCol = new TableColumn<>("Shares");
        sharesCol.setCellValueFactory(new PropertyValueFactory<>("shares"));
        
        TableColumn<Holding, Double> avgPriceCol = new TableColumn<>("Avg Price");
        avgPriceCol.setCellValueFactory(new PropertyValueFactory<>("averagePurchasePrice"));
        avgPriceCol.setCellFactory(column -> new TableCell<Holding, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        TableColumn<Holding, Double> currentPriceCol = new TableColumn<>("Current Price");
        currentPriceCol.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        currentPriceCol.setCellFactory(column -> new TableCell<Holding, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        TableColumn<Holding, Double> totalValueCol = new TableColumn<>("Total Value");
        totalValueCol.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        totalValueCol.setCellFactory(column -> new TableCell<Holding, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        TableColumn<Holding, Double> profitLossCol = new TableColumn<>("P/L");
        profitLossCol.setCellValueFactory(new PropertyValueFactory<>("profitLoss"));
        profitLossCol.setCellFactory(column -> new TableCell<Holding, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(null);
                }
                if (!empty && item != null) {
                    setText(String.format("$%.2f", item));
                    if (item > 0) {
                        setTextFill(Color.GREEN);
                    } else if (item < 0) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
        
        holdingsTable.getColumns().addAll(
            symbolCol, sharesCol, avgPriceCol, currentPriceCol, totalValueCol, profitLossCol
        );
        
        holdingsTable.setItems(holdingsList);
        
        // Add sell button
        Button sellButton = new Button("Sell Selected");
        sellButton.setOnAction(e -> sellSelectedHolding());
        
        holdingsContent.getChildren().addAll(holdingsTable, sellButton);
        holdingsTab.setContent(holdingsContent);
        
        // Watchlist Tab
        Tab watchlistTab = new Tab("Watchlist");
        watchlistTab.setClosable(false);
        
        VBox watchlistContent = new VBox(10);
        watchlistContent.setPadding(new Insets(15));
        
        watchlistTable = new TableView<>();
        watchlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<StockQuote, String> watchlistSymbolCol = new TableColumn<>("Symbol");
        watchlistSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        
        TableColumn<StockQuote, Double> watchlistPriceCol = new TableColumn<>("Price");
        watchlistPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        watchlistPriceCol.setCellFactory(column -> new TableCell<StockQuote, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        watchlistTable.getColumns().addAll(watchlistSymbolCol, watchlistPriceCol);
        watchlistTable.setItems(watchlistQuotes);
        
        // Add buy button
        Button buyButton = new Button("Buy Selected");
        buyButton.setOnAction(e -> buySelectedStock());
        
        watchlistContent.getChildren().addAll(watchlistTable, buyButton);
        watchlistTab.setContent(watchlistContent);
        
        tabPane.getTabs().addAll(holdingsTab, watchlistTab);
        
        return tabPane;
    }
    
    private VBox createBottomSection() {
        VBox bottomSection = new VBox(15);
        bottomSection.setPadding(new Insets(15));
        bottomSection.setStyle("-fx-background-color: #f0f0f0;");
        
        // Trading controls
        HBox tradingControls = new HBox(15);
        tradingControls.setAlignment(Pos.CENTER);
        
        // Symbol input
        VBox symbolBox = new VBox(5);
        Label symbolLabel = new Label("Symbol:");
        symbolField = new TextField();
        symbolBox.getChildren().addAll(symbolLabel, symbolField);
        
        // Shares input
        VBox sharesBox = new VBox(5);
        Label sharesLabel = new Label("Shares:");
        sharesField = new TextField();
        sharesBox.getChildren().addAll(sharesLabel, sharesField);
        
        // Price input
        VBox priceBox = new VBox(5);
        Label priceLabel = new Label("Price:");
        priceField = new TextField();
        priceBox.getChildren().addAll(priceLabel, priceField);
        
        // Buy button
        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> buyStock());
        
        // Sell button
        Button sellButton = new Button("Sell");
        sellButton.setOnAction(e -> sellStock());
        
        tradingControls.getChildren().addAll(symbolBox, sharesBox, priceBox, buyButton, sellButton);
        
        // Watchlist controls
        HBox watchlistControls = new HBox(15);
        watchlistControls.setAlignment(Pos.CENTER);
        
        // Watchlist symbol input
        VBox watchlistSymbolBox = new VBox(5);
        Label watchlistSymbolLabel = new Label("Add to Watchlist:");
        watchlistSymbolField = new TextField();
        watchlistSymbolBox.getChildren().addAll(watchlistSymbolLabel, watchlistSymbolField);
        
        // Add to watchlist button
        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setOnAction(e -> addToWatchlist());
        
        // Remove from watchlist button
        Button removeFromWatchlistButton = new Button("Remove from Watchlist");
        removeFromWatchlistButton.setOnAction(e -> removeFromWatchlist());
        
        watchlistControls.getChildren().addAll(watchlistSymbolBox, addToWatchlistButton, removeFromWatchlistButton);
        
        bottomSection.getChildren().addAll(tradingControls, watchlistControls);
        
        return bottomSection;
    }
    
    private void startBackgroundTasks() {
        scheduler = Executors.newScheduledThreadPool(1);
        
        // Update portfolio data every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                updatePortfolioSummary();
                updateWatchlist();
            });
        }, 0, 30, TimeUnit.SECONDS);
    }
    
    private void updatePortfolioSummary() {
        // Update holdings with current prices
        for (Holding holding : holdingsList) {
            double currentPrice = quoteService.fetchQuote(holding.getSymbol());
            if (currentPrice > 0) {
                holding.updateCalculatedValues(currentPrice);
            }
        }
        
        // Update UI
        cashBalanceLabel.setText(String.format("Cash: $%.2f", portfolio.getCashBalance()));
        totalValueLabel.setText(String.format("Total Value: $%.2f", portfolio.calculateTotalPortfolioValue()));
        
        double profitLoss = portfolio.calculateTotalProfitLoss();
        profitLossLabel.setText(String.format("P/L: $%.2f", profitLoss));
        if (profitLoss > 0) {
            profitLossLabel.setTextFill(Color.GREEN);
        } else if (profitLoss < 0) {
            profitLossLabel.setTextFill(Color.RED);
        } else {
            profitLossLabel.setTextFill(Color.BLACK);
        }
        
        // Refresh holdings table
        holdingsTable.refresh();
    }
    
    private void updateWatchlist() {
        watchlistQuotes.clear();
        
        for (String symbol : portfolio.getWatchlistSymbols()) {
            double price = quoteService.fetchQuote(symbol);
            if (price > 0) {
                watchlistQuotes.add(new StockQuote(symbol, price));
            }
        }
    }
    
    private void buyStock() {
        try {
            String symbol = symbolField.getText().trim().toUpperCase();
            int shares = Integer.parseInt(sharesField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            
            if (symbol.isEmpty()) {
                showAlert("Error", "Symbol cannot be empty.");
                return;
            }
            
            if (shares <= 0) {
                showAlert("Error", "Shares must be greater than 0.");
                return;
            }
            
            if (price <= 0) {
                showAlert("Error", "Price must be greater than 0.");
                return;
            }
            
            double totalCost = shares * price;
            
            if (totalCost > portfolio.getCashBalance()) {
                showAlert("Error", "Insufficient funds.");
                return;
            }
            
            // Check if we already have this stock
            Optional<Holding> existingHolding = portfolio.findHolding(symbol);
            
            if (existingHolding.isPresent()) {
                // Update existing holding
                Holding holding = existingHolding.get();
                int newShares = holding.getShares() + shares;
                double newAvgPrice = ((holding.getShares() * holding.getAveragePurchasePrice()) + (shares * price)) / newShares;
                
                holding.setShares(newShares);
                holding.setAveragePurchasePrice(newAvgPrice);
                holding.updateCalculatedValues(price);
                
                // Refresh the table
                holdingsTable.refresh();
            } else {
                // Create new holding
                Holding newHolding = new Holding(symbol, shares, price);
                newHolding.updateCalculatedValues(price);
                
                portfolio.addHolding(newHolding);
                holdingsList.add(newHolding);
            }
            
            // Update cash balance
            portfolio.decreaseCash(totalCost);
            
            // Save portfolio
            PortfolioManager.savePortfolio(portfolio);
            
            // Update UI
            updatePortfolioSummary();
            
            // Clear input fields
            symbolField.clear();
            sharesField.clear();
            priceField.clear();
            
            showAlert("Success", "Stock purchased successfully.");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for shares and price.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private void sellStock() {
        try {
            String symbol = symbolField.getText().trim().toUpperCase();
            int shares = Integer.parseInt(sharesField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            
            if (symbol.isEmpty()) {
                showAlert("Error", "Symbol cannot be empty.");
                return;
            }
            
            if (shares <= 0) {
                showAlert("Error", "Shares must be greater than 0.");
                return;
            }
            
            if (price <= 0) {
                showAlert("Error", "Price must be greater than 0.");
                return;
            }
            
            // Check if we have this stock
            Optional<Holding> existingHolding = portfolio.findHolding(symbol);
            
            if (!existingHolding.isPresent()) {
                showAlert("Error", "You don't own any shares of " + symbol);
                return;
            }
            
            Holding holding = existingHolding.get();
            
            if (shares > holding.getShares()) {
                showAlert("Error", "You don't have enough shares to sell.");
                return;
            }
            
            // Calculate proceeds
            double proceeds = shares * price;
            
            // Update holding
            int remainingShares = holding.getShares() - shares;
            
            if (remainingShares == 0) {
                // Remove the holding completely
                portfolio.removeHolding(holding);
                holdingsList.remove(holding);
            } else {
                // Update the holding
                holding.setShares(remainingShares);
                holding.updateCalculatedValues(price);
                
                // Refresh the table
                holdingsTable.refresh();
            }
            
            // Update cash balance
            portfolio.increaseCash(proceeds);
            
            // Save portfolio
            PortfolioManager.savePortfolio(portfolio);
            
            // Update UI
            updatePortfolioSummary();
            
            // Clear input fields
            symbolField.clear();
            sharesField.clear();
            priceField.clear();
            
            showAlert("Success", "Stock sold successfully.");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for shares and price.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private void sellSelectedHolding() {
        Holding selectedHolding = holdingsTable.getSelectionModel().getSelectedItem();
        
        if (selectedHolding == null) {
            showAlert("Error", "Please select a holding to sell.");
            return;
        }
        
        // Pre-fill the sell form
        symbolField.setText(selectedHolding.getSymbol());
        sharesField.setText(String.valueOf(selectedHolding.getShares()));
        priceField.setText(String.valueOf(selectedHolding.getCurrentPrice()));
    }
    
    private void buySelectedStock() {
        StockQuote selectedQuote = watchlistTable.getSelectionModel().getSelectedItem();
        
        if (selectedQuote == null) {
            showAlert("Error", "Please select a stock to buy.");
            return;
        }
        
        // Pre-fill the buy form
        symbolField.setText(selectedQuote.getSymbol());
        priceField.setText(String.valueOf(selectedQuote.getPrice()));
    }
    
    private void addToWatchlist() {
        String symbol = watchlistSymbolField.getText().trim().toUpperCase();
        
        if (symbol.isEmpty()) {
            showAlert("Error", "Symbol cannot be empty.");
            return;
        }
        
        // Verify the symbol exists by fetching a quote
        double price = quoteService.fetchQuote(symbol);
        
        if (price <= 0) {
            showAlert("Error", "Invalid symbol or unable to fetch quote.");
            return;
        }
        
        portfolio.addWatchlistSymbol(symbol);
        PortfolioManager.savePortfolio(portfolio);
        
        // Update watchlist
        updateWatchlist();
        
        // Clear input field
        watchlistSymbolField.clear();
        
        showAlert("Success", symbol + " added to watchlist.");
    }
    
    private void removeFromWatchlist() {
        String symbol = watchlistSymbolField.getText().trim().toUpperCase();
        
        if (symbol.isEmpty()) {
            showAlert("Error", "Symbol cannot be empty.");
            return;
        }
        
        portfolio.removeWatchlistSymbol(symbol);
        PortfolioManager.savePortfolio(portfolio);
        
        // Update watchlist
        updateWatchlist();
        
        // Clear input field
        watchlistSymbolField.clear();
        
        showAlert("Success", symbol + " removed from watchlist.");
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 