package handlers;

import classes.Composition;
import classes.Node;
import classes.PlayList;
import java.io.File;
import javafx.scene.control.Alert;
/*
Класс-помощник для обработки разных статических методов.
 */
public class Handler {
    private static File chosenFile;
    private static String playlistName;
    private static PlayList<Composition> playlist;
    private static Node<Composition> selectedNode;

    /*
    Метод-сеттер для выбранного нода
     */
    public static void setSelectedNode(Node<Composition> selectedNode) {
        Handler.selectedNode = selectedNode;
    }
    /*
    Метод-геттер для выбранного нода
     */
    public static Node<Composition> getSelectedNode() {
        return selectedNode;
    }
    /*
    Метод-сеттер для выбранного плейлиста(его названия)
     */
    public static void setPlaylistName(String playlistName) {
        Handler.playlistName = playlistName;
    }
    /*
    Метод-геттер для выбранного плейлиста(его названия)
     */
    public static String getPlaylistName() {
        return playlistName;
    }
    /*
    Метод-сеттер для выбранного файла
     */
    public static void setChosenFile(File file) {
        chosenFile = file;
    }
    /*
    Метод-геттер для выбранного файла
     */
    public static File getChosenFile() {
        return chosenFile;
    }
    /*
    Метод для открывания модального окошка в виде сообщения об ошибке
     */
    public static void throwErrorAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }
    /*
        Метод для открывания модального окошка в виде обычного сообщения
     */
    public static void throwInfoAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }
    /*
    Метод-геттер для выбранного плейлиста
     */
    public static PlayList<Composition> getPlaylist() {
        return playlist;
    }
    /*
    Метод-сеттер для выбранного плейлиста
     */
    public static void setPlaylist(PlayList<Composition> playlist) {
        Handler.playlist = playlist;
    }
}
