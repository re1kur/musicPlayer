package controllers;

import classes.Composition;
import classes.Node;
import classes.PlayList;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

/*
Класс-контролер окна dialogueChangePos
 */
public class DialogueChangePosController {
    @FXML
    private Label albumLabel;

    @FXML
    private Label artistLabel;

    @FXML
    private Button changeBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private ChoiceBox<Node<Composition>> posChoiceBox;

    @FXML
    private CheckBox swapCheckBox;

    @FXML
    private CheckBox afterCheckBox;

    @FXML
    private CheckBox beforeCheckBox;

    /*
    Стандартный метод Javafx для определения методов для контролов при инициализации
    аппликации.
     */
    @FXML
    void initialize() {
        setTracks(Handler.getPlaylist());
        nameLabel.setText(Handler.getSelectedNode().getValue().getName());
        artistLabel.setText(Handler.getSelectedNode().getValue().getArtists());
        albumLabel.setText(Handler.getSelectedNode().getValue().getAlbums());
        closeWindowBtn.setOnAction(_ -> closeWindow());
        changeBtn.setOnAction(_ -> changePos());
    }

    /*
    Сеттер треков для choicebox
     */
    private void setTracks(PlayList<Composition> playList) {
        playList.setCurrent(playList.getTail());
        while (playList.getCurrent().getPreNode() != playList.getTail()) {
            posChoiceBox.getItems().addFirst(playList.getCurrent());
            playList.turnLeftCurrent();
        }
        posChoiceBox.getItems().addFirst(playList.getCurrent());
        playList.setCurrent(playList.getHead());
    }

    /*
    Метод для изменения позиции трека
     */
    private void changePos() {
        if (posChoiceBox.getSelectionModel().isEmpty()) {
            Handler.throwErrorAlert(
                    "COULD NOT CHANGE POSITION", "Select a track first.");
            closeWindow();
            return;
        }
        if (!checkCheckBoxes()) {
            Handler.throwErrorAlert("COULD NOT CHANGE POSITION",
                    "Select only the one position where track will be changed.");
            closeWindow();
            return;
        }
        if (beforeCheckBox.isSelected()) {
            Handler.getPlaylist().moveBefore(Handler.getSelectedNode(),
                    posChoiceBox.getSelectionModel().getSelectedItem());
        }
        if (afterCheckBox.isSelected()) {
            Handler.getPlaylist().moveAfter(Handler.getSelectedNode(),
                    posChoiceBox.getSelectionModel().getSelectedItem());
        }
        if (swapCheckBox.isSelected()) {
            Handler.getPlaylist().swapNodes(Handler.getSelectedNode(),
                    posChoiceBox.getSelectionModel().getSelectedItem());
        }
        closeWindow();
    }

    /*
    Метод для закрытия окна
     */
    private void closeWindow() {
        albumLabel.getScene().getWindow().hide();
    }

    /*
    Метод для проверки нажатия чек боксов
     */
    private boolean checkCheckBoxes() {
        int selectedCount = 0;
        if (afterCheckBox.isSelected())
            selectedCount++;
        if (beforeCheckBox.isSelected())
            selectedCount++;
        if (swapCheckBox.isSelected())
            selectedCount++;
        return selectedCount == 1;
    }
}
