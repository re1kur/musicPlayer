package controllers;

import handlers.DatabaseHandler;
import handlers.Handler;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
/*
Класс-контроллер окна dialoguePlaylist для обработки нажатий на разные кнопки
 */
public class DialoguePlaylistController {
    @FXML private Button closeWindowBtn;

    @FXML private Button addPlaylistBtn;

    @FXML private TextField nameTextField;
    /*
    Стандартный метод Javafx для определения методов для контролов при инициализации
    аппликации.
     */
    @FXML
    void initialize() {
        addPlaylistBtn.setOnAction(_ -> addPlaylistIntoBD());
        closeWindowBtn.setOnAction(_ -> closeWindow());
    }
    /*
    Метод добавления плейлиста в бд
     */
    private void addPlaylistIntoBD() {
        if (nameTextField.getText().isEmpty()) {
            Handler.throwErrorAlert(
                    "TEXT FIELD IS EMPTY", "First enter the text fields and try again");
            return;
        }
        try {
            DatabaseHandler.executeUpdate("CREATE TABLE " + nameTextField.getText()
                    + " (id SERIAL NOT NULL PRIMARY KEY,"
                    + "name TEXT NOT NULL,"
                    + "artists TEXT NOT NULL,"
                    + "albums TEXT NOT NULL,"
                    + "id_track INT,"
                    + "FOREIGN KEY (id_track) REFERENCES uploaded_tracks(id_track)"
                    + ");");
        } catch (SQLException e) {
            Handler.throwErrorAlert("INVALID NAME FOR PLAYLIST",
                    "Enter the name for playlist without numeric characters and "
                            + "backspaces.");
            return;
        }
        try {
            DatabaseHandler.executeUpdate("INSERT INTO playlists"
                    + " (playlist_name) VALUES ('" + nameTextField.getText() + "')");
        } catch (SQLException e) {
            System.err.println("Could not insert" + nameTextField.getText()
                    + " row in playlists:\n" + e.getMessage());
            try {
                DatabaseHandler.executeUpdate(
                        "DROP TABLE " + nameTextField.getText() + ";");
                Handler.throwErrorAlert("SOMETHING WENT WRONG",
                        "Something went wrong, playlist was deleted.\nTry again.");
                return;
            } catch (SQLException e1) {
                System.err.println("Could not drop table " + nameTextField.getText()
                        + ":\n" + e1.getMessage());
            }
        }
        Handler.throwInfoAlert("SUCCESSFULLY",
                "The playlist has been successfully created.\nEnjoy the music.");
        closeWindow();
    }
    /*
    Метод закрытия окна
     */
    private void closeWindow() {
        nameTextField.getScene().getWindow().hide();
    }
}
