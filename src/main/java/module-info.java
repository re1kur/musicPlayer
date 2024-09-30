module musicPlayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.sql;
    requires minio;
    opens project to javafx.fxml;
    opens project.controllers to javafx.fxml, javafx.graphics, javafx.controls, javafx.media, java.sql;
    opens project.handlers to minio;
    opens project.classes to javafx.graphics;
}