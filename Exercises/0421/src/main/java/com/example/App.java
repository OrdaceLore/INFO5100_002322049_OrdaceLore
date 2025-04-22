package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        Label welcomeLabel = new Label("Welcome to JavaFX!");
        Button clickButton = new Button("Click Me!");
        
        // Add button click handler
        clickButton.setOnAction(e -> {
            welcomeLabel.setText("Button was clicked!");
        });

        // Create layout
        VBox root = new VBox(10); // 10 pixels spacing
        root.getChildren().addAll(welcomeLabel, clickButton);
        root.setStyle("-fx-padding: 20;"); // Add some padding

        // Create scene
        Scene scene = new Scene(root, 300, 200);
        
        // Set up stage
        primaryStage.setTitle("JavaFX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 