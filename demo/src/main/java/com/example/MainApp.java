package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up the main container with BorderPane layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2E2E2E;"); // Dark background color for the main area

        // Create the sidebar
        VBox sidebar = createSidebar(root); // Pass root to sidebar creation
        root.setLeft(sidebar);

        // Create the main content area (initial content message)
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #3A3A3A;");
        root.setCenter(contentArea);

        // Initial content (message)
        contentArea.getChildren().add(new Button("Welcome to the Dark Mode App"));

        // Set up the scene with dark background
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle("Dark Mode JavaFX GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Function to create the sidebar and handle button actions
    private VBox createSidebar(BorderPane root) {
        VBox sidebar = new VBox(10);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 20;"); // Dark sidebar color

        // Create buttons for the sidebar
        Button btn1 = createSidebarButton("Home");
        Button btn2 = createSidebarButton("Settings");
        Button btn3 = createSidebarButton("Profile");

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(btn1, btn2, btn3);

        // Add action listeners to change content based on sidebar buttons
        btn1.setOnAction(event -> changeContent(root, "Home Content"));
        btn2.setOnAction(event -> changeContent(root, "Settings Content"));
        btn3.setOnAction(event -> changeContent(root, "Profile Content"));

        return sidebar;
    }

    // Function to create buttons with a consistent style
    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #3A3A3A; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5;");
        button.setPrefWidth(200);
        button.setMaxHeight(40);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555555; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #3A3A3A; -fx-text-fill: white;"));
        return button;
    }

    // Function to update the main content based on the button clicked
    private void changeContent(BorderPane root, String content) {
        StackPane contentArea = (StackPane) root.getCenter(); // Access the content area from the root pane
        contentArea.getChildren().clear(); // Clear the existing content
        contentArea.getChildren().add(new Button(content)); // Add new content
    }

    public static void main(String[] args) {
        launch(args);
    }
}