package handlers;


import classes.Composition;
import javafx.scene.control.Alert;

import java.io.File;

public class Handler {
    private static File chosenFile;
    private static String playlist;
    private static Composition selectedComposition;

    public static void setSelectedComposition (Composition selectedComposition) {
        Handler.selectedComposition = selectedComposition;
    }

    public static Composition getSelectedComposition () {
        return selectedComposition;
    }

    public static void setPlaylist(String playlist) {
        Handler.playlist = playlist;
    }

    public static String getPlaylist() {
        return playlist;
    }

    public static void setChosenFile(File file) {
        chosenFile = file;
    }

    public static File getChosenFile () {
        return chosenFile;
    }

    public static void throwErrorAlert (String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public static void throwInfoAlert (String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }

}
