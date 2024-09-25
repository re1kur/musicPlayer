package controllers;

import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DialogueTrackController {

    @FXML
    private TextField albumsTextField;

    @FXML
    private TextField artistsTextField;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Label fileNameLabel;

    @FXML
    private Button insertTrackIntoBtn;

    @FXML
    private Label pathLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    void initialize () {
        pathLabel.setText(getFilePath());
        fileNameLabel.setText(getFileName());
        closeWindowBtn.setOnAction(_ -> albumsTextField.getScene().getWindow().hide());
        insertTrackIntoBtn.setOnAction(_ -> insertTrackIntoBD());

    }

    private String getFileName () {
        return Handler.getChosenFile().getName();
    }

    private String getFilePath () {
        return Handler.getChosenFile().getAbsolutePath();
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

    private void insertTrackIntoBD () {
        if (!checkFields()) {
            Handler.throwErrorAlert("TEXT FIELD IS EMPTY",
                    "First enter the text fields and try again");
            return;
        }
        DatabaseHandler.uploadTrack(Handler.getPlaylist(), nameTextField.getText(),
                artistsTextField.getText(), albumsTextField.getText(), Handler.getChosenFile().getAbsolutePath());
        artistsTextField.getScene().getWindow().hide();
    }


    
}
