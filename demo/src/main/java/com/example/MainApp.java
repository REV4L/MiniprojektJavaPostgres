package com.example;

import javafx.scene.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class MainApp extends Application {

    public static String appName = "VPIK (Miniprojekt)";
    public Stage primaryStage = null;

    @Override
    public void start(Stage ps) {
        try {
            Database.connect();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        primaryStage = ps;
        rebuild(ps);
    }

    void rebuild() {
        rebuild(primaryStage);
    }

    void rebuild(Stage primaryStage) {
        // Set up the main container with BorderPane layout
        BorderPane root = new BorderPane();
        // root.setStyle("-fx-background-color: #2E2E2E;"); // Dark background color for
        // the main area
        // root.setStyle("-fx-font-family: \"Comic Sans MS\";");

        // Create the sidebar

        // Create the main content area (initial content message)
        StackPane stack = new StackPane();
        stack.setStyle("-fx-background-color: #3A3A3A;");
        root.setCenter(stack);

        if (Database.loggedIn()) {
            VBox sidebar = leftSidebar(root); // Pass root to sidebar creation
            root.setLeft(sidebar);
        } else {
            // Initial content (message)
            stack.getChildren().add(pageLogin());
        }

        // Set up the scene with dark background
        Scene scene = new Scene(root, 1400, 800);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle(appName);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Label userLabel = null;

    // Function to create the sidebar and handle button actions
    private VBox leftSidebar(BorderPane root) {
        VBox sidebar = new VBox(10);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 20;"); // Dark sidebar color

        // Create buttons for the sidebar

        Label userLabel = new Label(
                Database.organizator != null ? "Logged in as " + Database.organizator.ime : "Log In");

        HBox p = new HBox();
        p.minHeight(200);
        p.getStyleClass().add("container");
        p.setAlignment(Pos.CENTER);
        p.getChildren().add(userLabel);

        Button b1 = newStdButton("Profil");
        Button b2 = newStdButton("Nastavitve");
        Button b3 = newStdButton("Dogodki");
        Button b4 = newStdButton("Prostori");
        Button b5 = newStdButton("Izvajalci");
        Button logOut = newStdButton("LOG OUT");
        logOut.setStyle("-fx-background-color: rgb(218, 70, 70)");

        Region s = new Region();
        VBox.setVgrow(s, Priority.ALWAYS);

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(p, b1, b2, b3, b4, b5, s, logOut);

        b1.setOnAction(event -> changeContent(root, "Home Content"));
        b2.setOnAction(event -> changeContent(root, "Settings Content"));
        b3.setOnAction(event -> changeContent(root, "Profile Content"));
        logOut.setOnAction(event -> {
            Database.logOut();
            rebuild();
        });

        return sidebar;
    }

    // Function to create buttons with a consistent style
    private Button newStdButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        // button.setStyle(
        // "-fx-background-color: #3A3A3A; -fx-text-fill: white; -fx-font-size: 20px;
        // -fx-border-radius: 5;");
        button.setPrefWidth(200);
        button.setMaxHeight(40);

        // button.setStyle("-fx-background-color: #3A3A3A; -fx-text-fill: white;");
        // button.setOnMouseEntered(e -> button.setStyle(
        // "-fx-background-color:rgb(77, 77, 77); -fx-text-fill: white; -fx-font-size:
        // 20px; -fx-border-radius: 5;"));
        // button.setOnMouseExited(e -> button.setStyle(
        // "-fx-background-color: #3A3A3A; -fx-text-fill: white; -fx-font-size: 20px;
        // -fx-border-radius: 5;"));
        return button;
    }

    private VBox pageLogin() {
        VBox page = new VBox(10); // Adds spacing between elements
        page.setAlignment(Pos.CENTER);
        page.setStyle("-fx-background-color:rgb(66, 66, 66); -fx-padding: 20px;"); // Dark background with padding

        Label emailLabel = new Label("Email:");
        // emailLabel.setTextFill(Color.WHITE);
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(300);
        // emailField.setStyle("-fx-background-color:rgb(54, 54, 54);
        // -fx-text-fill:rgb(255, 255, 255);");

        Label passwordLabel = new Label("Password:");
        // passwordLabel.setTextFill(Color.WHITE);
        passwordLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300);
        // passwordField.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill:
        // #2c3e50;");

        Button loginButton = newStdButton("Log In");
        loginButton.setStyle("-fx-background-color: #3498db;");

        // Add event handler to the button
        loginButton.setOnAction(event -> {
            // Get the values from the text fields
            String email = emailField.getText();
            String password = passwordField.getText();

            // Validate the inputs (optional)
            if (email.isEmpty() || password.isEmpty()) {
                // Show an alert or handle empty input case
                // System.out.println("Please fill out both fields.");
                return;
            }

            // Execute your query here with email and password
            // Example query logic (this is where you'd perform your logic)
            if (Database.login(email, password)) {
                rebuild();
                // userLabel.setText(Database.organizator.ime);
            }
        });

        page.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, loginButton);

        return page;
    }

    // boolean tryLogin(String email, String pasw) {
    // return false;
    // }

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