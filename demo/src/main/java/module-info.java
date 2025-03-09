module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.sql;

    opens com.example to javafx.fxml;

    requires static java.dotenv;

    exports com.example;
}