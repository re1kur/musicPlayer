package controllers;

import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/*
Класс-контроллер окна dialogueEdit для обработки нажатий на разные кнопки
 */
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

    /*
    Стандартный метод Javafx для определения методов для контролов при инициализации
    аппликации.
     */
    @FXML
    void initialize() {
        nameTextField.setText(Handler.getSelectedNode().getValue().getName());
        artistsTextField.setText(Handler.getSelectedNode().getValue().getArtists());
        albumsTextField.setText(Handler.getSelectedNode().getValue().getAlbums());
        closeWindowBtn.setOnAction(_ -> closeWindow());
        editTrackBtn.setOnAction(_ -> editTrackBD());
    }

    /*
    Метод для закрытия окна
     */
    private void closeWindow() {
        artistsTextField.getScene().getWindow().hide();
    }

    /*
    Метод редактирования трека в бд
     */
    private void editTrackBD() {
        if (!checkFields()) {
            Handler.throwErrorAlert(
                    "TEXT FIELD IS EMPTY", "First enter the text fields and try again");
            return;
        }
        DatabaseHandler.editTrack(Handler.getPlaylistName(),
                Handler.getSelectedNode().getValue().getId(), nameTextField.getText(),
                artistsTextField.getText(), albumsTextField.getText());
        closeWindow();
    }

    /*
    Метод для проверки текстовых полей
     */
    private Boolean checkFields() {
        if (nameTextField.getText().isEmpty()) {
            return false;
        }
        if (artistsTextField.getText().isEmpty()) {
            return false;
        }
        return !albumsTextField.getText().isEmpty();
    }
}
