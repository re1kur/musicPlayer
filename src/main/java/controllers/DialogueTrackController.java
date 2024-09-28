package controllers;

import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/*
Класс-контроллер окна dialogueTrack для обработки нажатий на разные кнопки
 */
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

    /*
    Стандартный метод Javafx для определения методов для контролов при инициализации
    аппликации.
     */
    @FXML
    void initialize() {
        pathLabel.setText(getFilePath());
        fileNameLabel.setText(getFileName());
        closeWindowBtn.setOnAction(_ -> closeWindow());
        insertTrackIntoBtn.setOnAction(_ -> insertTrackIntoBD());
    }

    /*
    Метод-геттер для получения выбранного файла
     */
    private String getFileName() {
        return Handler.getChosenFile().getName();
    }

    /*
    Метод-геттер для получения абсолютного пути выбранного файла
     */
    private String getFilePath() {
        return Handler.getChosenFile().getAbsolutePath();
    }

    /*
    Метод проверки текстовых полей
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

    /*
    Метод вставки трека в бд
     */
    private void insertTrackIntoBD() {
        if (!checkFields()) {
            Handler.throwErrorAlert(
                    "TEXT FIELD IS EMPTY", "First enter the text fields and try again");
            return;
        }
        DatabaseHandler.uploadTrack(Handler.getPlaylistName(),
                nameTextField.getText(), artistsTextField.getText(),
                albumsTextField.getText(), Handler.getChosenFile().getAbsolutePath());
        closeWindow();
    }

    /*
    Метод закрытия окна
     */
    private void closeWindow() {
        albumsTextField.getScene().getWindow().hide();
    }
}
