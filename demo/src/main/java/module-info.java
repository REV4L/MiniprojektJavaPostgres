module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.sql;

    opens com.example to javafx.fxml;

    exports com.example;
}