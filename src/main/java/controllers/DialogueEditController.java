package controllers;

import classes.Composition;
import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialogueEditController {
    @FXML
    private TextField albumsTextField;

    @FXML
    private TextField artistsTextField;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button editTrackBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    void initialize() {
        nameTextField.setText(Handler.getSelectedComposition().getName());
        artistsTextField.setText(Handler.getSelectedComposition().getArtists());
        albumsTextField.setText(Handler.getSelectedComposition().getAlbums());
        closeWindowBtn.setOnAction(_ -> closeWindow());
        editTrackBtn.setOnAction(_ -> editTrackBD());
    }

    private void closeWindow() {
        artistsTextField.getScene().getWindow().hide();
    }

    private void editTrackBD () {
        if (!checkFields()) {
            Handler.throwErrorAlert("TEXT FIELD IS EMPTY",
                    "First enter the text fields and try again");
            return;
        }
        DatabaseHandler.editTrack(Handler.getPlaylist(),
                Handler.getSelectedComposition().getId(),
                nameTextField.getText(), artistsTextField.getText(),
                albumsTextField.getText());
        closeWindow();
    }

    private Boolean checkFields () {
        if (nameTextField.getText().isEmpty()) {
            return false;
        } if (artistsTextField.getText().isEmpty()) {
            return false;
        } if (albumsTextField.getText().isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

}
