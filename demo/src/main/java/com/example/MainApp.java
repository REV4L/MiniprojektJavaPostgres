package com.example;

import javafx.scene.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import java.util.List;

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

    BorderPane root;

    void rebuild(Stage primaryStage) {
        // Set up the main container with BorderPane layout
        root = new BorderPane();
        // root.setStyle("-fx-background-color: #2E2E2E;"); // Dark background color for
        // the main area
        // root.setStyle("-fx-font-family: \"Comic Sans MS\";");

        // Create the sidebar

        // Create the main content area (initial content message)
        Database.login("val", "val");

        StackPane stack = new StackPane();
        stack.setStyle("-fx-background-color: #3A3A3A;");
        root.setCenter(stack);

        if (Database.loggedIn()) {
            Database.getOrganizator();
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

        changeContent(pageIzvajalci());
        // debug
    }

    // Label userLabel = null;

    // Function to create the sidebar and handle button actions
    private VBox leftSidebar(BorderPane root) {
        VBox sidebar = new VBox(10);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 20;"); // Dark sidebar color

        // Create buttons for the sidebar

        Label userLabel = new Label(
                Database.organizator != null ? Database.organizator.ime : "Log In");
        userLabel.getStyleClass().add("userlabel");
        // Label userLabel = new Label(
        // Database.organizator != null ? "Logged in as \n " + Database.organizator.ime
        // : "Log In");

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

        sidebar.setMaxWidth(300);

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(p, b1, b2, b3, b4, b5, s, logOut);

        b1.setOnAction(event -> changeContent(pageOrganizator()));
        b5.setOnAction(event -> changeContent(pageIzvajalci()));
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

        // for (Kraj k : krajiList) {
        // System.out.println("Loaded: " + k);
        // }
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

        VBox page = new VBox(10); // Adds spacing between elements
        page.setAlignment(Pos.CENTER_RIGHT);
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
        HBox hOpis = new HBox(10);
        Label opisLabel = new Label("Opis:");

        TextArea opisField = new TextArea();
        opisField.setPromptText("Opis...");
        opisField.setMaxWidth(3000);
        opisField.setPrefRowCount(5); // Adjusts height
        opisField.setWrapText(true); // Enables text wrapping
        opisField.setText(Database.organizator.opis);

        Region regionOpis = new Region();
        regionOpis.setMinWidth(100);
        // HBox.setHgrow(regionOpis, Priority.min(30, 30));
        HBox.setHgrow(opisField, Priority.ALWAYS);

        hOpis.getChildren().addAll(opisLabel, regionOpis, opisField);
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

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        saveButton.setOnAction(e -> {

            int krajId = krajCB.getSelectionModel().getSelectedItem().id;

            Database.updateOrganizator(Database.organizatorId,
                    imeField.getText(),
                    opisField.getText(),
                    emailField.getText(),
                    telefonField.getText(),
                    naslovField.getText(),
                    krajId);

            rebuild();

            // Update the database with the new values (you need to create an update method
            // in Database)
            // Database.updateOrganizator(Database.organizator);

            // Feedback to the user (optional)
            System.out.println("Profile updated successfully!");
        });

        // Add all fields and button to the page

        hIme.getStyleClass().add("group");
        hOpis.getStyleClass().add("group");
        hEmail.getStyleClass().add("group");
        hTelefon.getStyleClass().add("group");
        hNaslov.getStyleClass().add("group");
        hKraj.getStyleClass().add("group");
        // hSettingsId.getStyleClass().add("group");
        // hStDogodkov.getStyleClass().add("group");
        page.getChildren().addAll(hIme, hOpis, hEmail, hTelefon, hNaslov, hKraj, saveButton);

        scrollPane.setContent(page);
        scrollPane.setFitToWidth(true); // Ensures it resizes properly
        scrollPane.setFitToHeight(true); // Ensures it resizes properly
        scrollPane.setPannable(true); // Allows mouse scrolling

        // return scrollPane;
        return scrollPane;
    }

    public Node pageIzvajalci() {
        // List all Izvajalci from the database
        ObservableList<Izvajalec> izvajalciList = Database.getIzvajalci();

        ScrollPane scrollPane = new ScrollPane();

        // VBox to hold the content
        VBox page = new VBox(10);
        page.setPadding(new Insets(10));
        page.getStyleClass().add("page");

        // Create "Add New" button
        Button addNewButton = new Button("Add New");
        addNewButton.setOnAction(event -> {
            // Insert blank izvajalec and get the ID
            int newId = Database.insertIzvajalec("New Izvajalec", "", ""); // Insert blank values

            System.out.println(newId);
            // After inserting, go to the edit page for this new ID
            changeContent(pageEditIzvajalec(newId)); // Call the pageEditIzvajalec method to navigate
        });

        // Add the button at the top of the page
        page.getChildren().add(addNewButton);

        // Display all izvajalci with their edit buttons
        for (Izvajalec izvajalec : izvajalciList) {
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);

            Label imeLabel = new Label(izvajalec.ime); // Directly accessing public field
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> changeContent(pageEditIzvajalec(izvajalec.id))); // Edit button redirects to
                                                                                         // edit page

            hbox.getChildren().addAll(imeLabel, editButton);
            page.getChildren().add(hbox);
        }

        scrollPane.setContent(page);
        scrollPane.setFitToWidth(true); // Ensures it resizes properly
        scrollPane.setFitToHeight(true); // Ensures it resizes properly
        scrollPane.setPannable(true); // Allows mouse scrolling

        return scrollPane;
    }

    public Node pageEditIzvajalec(int id) {
        // Get the Izvajalec data by ID
        Izvajalec izvajalec = Database.getIzvajalec(id);

        // System.out.println(izvajalec);

        // VBox to hold the content
        VBox page = new VBox(10);
        page.setPadding(new Insets(10));

        // Create fields for editing
        TextField imeField = new TextField(izvajalec.ime); // Directly accessing public field
        imeField.setPromptText("Ime");

        TextArea opisField = new TextArea(izvajalec.opis); // Directly accessing public field
        opisField.setPromptText("Opis");

        TextField telefonField = new TextField(izvajalec.telefon); // Directly accessing public field
        telefonField.setPromptText("Telefon");

        // Save button
        Button backButton = new Button("Back");
        Button saveButton = new Button("Save Changes");
        backButton.setOnAction(event -> {
            changeContent(pageIzvajalci());
        });
        saveButton.setOnAction(event -> {
            // Get updated data
            String ime = imeField.getText();
            String opis = opisField.getText();
            String telefon = telefonField.getText();

            // Update the izvajalec in the database
            Database.updateIzvajalec(id, ime, opis, telefon);
        });

        // Add elements to the page
        page.getChildren().addAll(
                new Label("Ime: "), imeField,
                new Label("Opis: "), opisField,
                new Label("Telefon: "), telefonField,
                saveButton);

        return page;
    }

    private void changeContent(Node page) {
        Database.getOrganizator(Database.organizatorId);

        StackPane contentArea = (StackPane) root.getCenter(); // Access the content area from the root pane
        contentArea.getChildren().clear(); // Clear the existing content
        contentArea.getChildren().add(page); // Add new content
    }

    public static void main(String[] args) {
        launch(args);
    }
}