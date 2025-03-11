package com.example;

import javafx.scene.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
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
        primaryStage.setResizable(false);
        primaryStage.show();

        // debug
        Database.login("val", "val");
        changeContent(root, pageOrganizator());
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

        b1.setOnAction(event -> changeContent(root, pageOrganizator()));
        // b2.setOnAction(event -> changeContent(root, "Settings Content"));
        // b3.setOnAction(event -> changeContent(root, "Profile Content"));
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
        VBox page = new VBox(1); // Adds spacing between elements
        page.setAlignment(Pos.CENTER);
        page.getStyleClass().add("page");
        // page.setStyle("-fx-background-color:rgb(66, 66, 66); -fx-padding: 20px;"); //
        // Dark background with padding

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

    private ComboBox<Kraj> krajCombobox(int preselectedId) {
        ComboBox<Kraj> krajDropdown = new ComboBox<>();
        krajDropdown.setPromptText("Izberi kraj...");
        krajDropdown.setMaxWidth(300);

        // Fetch kraji using getKraji function
        ObservableList<Kraj> krajiList = FXCollections
                .observableArrayList(Database.getKraji());

        for (Kraj k : krajiList) {
            System.out.println("Loaded: " + k);
        }
        krajDropdown.setItems(krajiList);

        // Set the currently selected Kraj (if available)
        krajiList.stream()
                .filter(k -> k.id == preselectedId)
                .findFirst()
                .ifPresent(krajDropdown::setValue);

        // Handle selection change
        krajDropdown.setOnAction(e -> {
            Kraj selectedKraj = krajDropdown.getValue();
            if (selectedKraj != null) {
                Database.organizator.krajId = selectedKraj.id;
            }
        });

        return krajDropdown;

    }

    private Node pageOrganizator() {
        ScrollPane scrollPane = new ScrollPane();

        VBox page = new VBox(50); // Adds spacing between elements
        page.setAlignment(Pos.CENTER_LEFT);
        page.getStyleClass().add("page");

        // ----
        HBox hIme = new HBox(10);
        Label imeLabel = new Label("Ime:");

        TextField imeField = new TextField();
        imeField.setPromptText("Ime...");
        imeField.setMaxWidth(300);
        imeField.setText(Database.organizator.ime);

        Region regionIme = new Region(); // Used for even spacing
        HBox.setHgrow(regionIme, Priority.ALWAYS);

        hIme.getChildren().addAll(imeLabel, regionIme, imeField);
        // ----

        // ----
        HBox hEmail = new HBox(10);
        Label emailLabel = new Label("Email:");

        TextField emailField = new TextField();
        emailField.setPromptText("Email...");
        emailField.setMaxWidth(300);
        emailField.setText(Database.organizator.email);

        Region regionEmail = new Region();
        HBox.setHgrow(regionEmail, Priority.ALWAYS);

        hEmail.getChildren().addAll(emailLabel, regionEmail, emailField);
        // ----

        // ----
        HBox hTelefon = new HBox(10);
        Label telefonLabel = new Label("Telefon:");

        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon...");
        telefonField.setMaxWidth(300);
        telefonField.setText(Database.organizator.telefon);

        Region regionTelefon = new Region();
        HBox.setHgrow(regionTelefon, Priority.ALWAYS);

        hTelefon.getChildren().addAll(telefonLabel, regionTelefon, telefonField);
        // ----

        // ----
        HBox hNaslov = new HBox(10);
        Label naslovLabel = new Label("Naslov:");

        TextField naslovField = new TextField();
        naslovField.setPromptText("Naslov...");
        naslovField.setMaxWidth(300);
        naslovField.setText(Database.organizator.naslov);

        Region regionNaslov = new Region();
        HBox.setHgrow(regionNaslov, Priority.ALWAYS);

        hNaslov.getChildren().addAll(naslovLabel, regionNaslov, naslovField);
        // ----

        // ----
        HBox hKraj = new HBox(10);
        Label krajIdLabel = new Label("Kraj:");

        ComboBox<Kraj> krajCB = krajCombobox(Database.organizator.krajId);

        // TextField krajIdField = new TextField();
        // krajIdField.setPromptText("Kraj ID...");
        // krajIdField.setMaxWidth(300);
        // krajIdField.setText(String.valueOf(Database.organizator.krajId));

        Region regionKrajId = new Region();
        HBox.setHgrow(regionKrajId, Priority.ALWAYS);

        hKraj.getChildren().addAll(krajIdLabel, regionKrajId, krajCB);
        // ----

        // ----
        HBox hSettingsId = new HBox(10);
        Label settingsIdLabel = new Label("Settings ID:");

        TextField settingsIdField = new TextField();
        settingsIdField.setPromptText("Settings ID...");
        settingsIdField.setMaxWidth(300);
        settingsIdField.setText(String.valueOf(Database.organizator.settingsId));

        Region regionSettingsId = new Region();
        HBox.setHgrow(regionSettingsId, Priority.ALWAYS);

        hSettingsId.getChildren().addAll(settingsIdLabel, regionSettingsId, settingsIdField);
        // ----

        // ----
        HBox hStDogodkov = new HBox(10);
        Label stDogodkovLabel = new Label("Št. dogodkov:");

        TextField stDogodkovField = new TextField();
        stDogodkovField.setPromptText("Št. dogodkov...");
        stDogodkovField.setMaxWidth(300);
        stDogodkovField.setText(String.valueOf(Database.organizator.stDogodkov));

        Region regionStDogodkov = new Region();
        HBox.setHgrow(regionStDogodkov, Priority.ALWAYS);

        hStDogodkov.getChildren().addAll(stDogodkovLabel, regionStDogodkov, stDogodkovField);
        // ----

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        saveButton.setOnAction(e -> {

            int krajId = krajCB.getSelectionModel().getSelectedItem().getId();

            Database.updateOrganizator(Database.organizatorId,
                    imeField.getText(),
                    emailField.getText(),
                    telefonField.getText(),
                    naslovField.getText());

            // Update the database with the new values (you need to create an update method
            // in Database)
            // Database.updateOrganizator(Database.organizator);

            // Feedback to the user (optional)
            System.out.println("Profile updated successfully!");
        });

        // Add all fields and button to the page

        hIme.getStyleClass().add("group");
        hEmail.getStyleClass().add("group");
        hTelefon.getStyleClass().add("group");
        hNaslov.getStyleClass().add("group");
        hKraj.getStyleClass().add("group");
        // hSettingsId.getStyleClass().add("group");
        hStDogodkov.getStyleClass().add("group");
        page.getChildren().addAll(hIme, hEmail, hTelefon, hNaslov, hKraj, hStDogodkov, saveButton);

        scrollPane.setContent(page);
        scrollPane.setFitToWidth(true); // Ensures it resizes properly
        scrollPane.setPannable(true); // Allows mouse scrolling

        // return scrollPane;
        return scrollPane;
    }

    // boolean tryLogin(String email, String pasw) {
    // return false;
    // }

    // Function to update the main content based on the button clicked
    private void changeContent(BorderPane root, Node page) {
        Database.getOrganizator(Database.organizatorId);

        StackPane contentArea = (StackPane) root.getCenter(); // Access the content area from the root pane
        contentArea.getChildren().clear(); // Clear the existing content
        contentArea.getChildren().add(page); // Add new content
    }

    public static void main(String[] args) {
        launch(args);
    }
}