package controllers;

import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialoguePlaylistController {

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button addPlaylistBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    void initialize () {
        addPlaylistBtn.setOnAction(_ -> addPlaylistIntoBD());
        closeWindowBtn.setOnAction(_ -> closeWindow());
    }

    private void addPlaylistIntoBD () {
        if (nameTextField.getText().isEmpty()) {
            Handler.throwErrorAlert("TEXT FIELD IS EMPTY",
                    "First enter the text fields and try again");
            return;
        }
        DatabaseHandler.executeUpdate("CREATE TABLE " + nameTextField.getText()
                + " (id SERIAL NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "artists TEXT NOT NULL," +
                "albums TEXT NOT NULL," +
                "id_track INT," +
                "FOREIGN KEY (id_track) REFERENCES uploaded_tracks(id_track)" +
                ");");
        DatabaseHandler.executeUpdate("INSERT INTO playlists" +
                " (playlist_name) VALUES ('" + nameTextField.getText() + "')");
        Handler.throwInfoAlert("SUCCESSFULLY", "The playlist has been successfully created.\nEnjoy the music.");
        closeWindow();
    }
    private void closeWindow () {
        nameTextField.getScene().getWindow().hide();
    }
}
