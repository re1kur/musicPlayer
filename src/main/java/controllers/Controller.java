package controllers;

import classes.Composition;
import classes.PlayList;
import handlers.DatabaseHandler;
import handlers.Handler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Button nextTrackBtn;

    @FXML
    private Button pauseTrackBtn;

    @FXML
    private ChoiceBox<String> playlistChoiceBox;

    @FXML
    private Button prevTrackBtn;

    @FXML
    private ChoiceBox<Composition> trackChoiceBox;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Button openFileChooserBtn;

    @FXML
    private AnchorPane tracksAnchorPane;

    @FXML
    private Button addPlaylistBtn;

    @FXML
    void initialize() {
        addPlaylistBtn.setOnAction(_ -> openDialoguePlaylist());
        playlistChoiceBox.getItems().addAll(getPlaylists());
        playlistChoiceBox.setOnAction(_ -> getTracks());
        closeWindowBtn.setOnAction(_ -> System.exit(0));
        openFileChooserBtn.setOnAction(_ -> openFileChooser());
        trackChoiceBox.setOnAction(_ -> selectTrack());

    }

    private void selectTrack () {
        System.out.println("The track is selected.");
    }

    private void setTracks(PlayList<Composition> playList) {
        playList.setCurrent(playList.getTail());
        while (playList.getCurrent().getPreNode() != playList.getTail()) {
                trackChoiceBox.getItems().addFirst(playList.getCurrent().getValue());
                playList.turnLeftCurrent();}
        trackChoiceBox.getItems().addFirst(playList.getCurrent().getValue());
        playList.setCurrent(playList.getHead());
    }

    private void getTracks () {
        trackChoiceBox.getItems().clear();
        Handler.setPlaylist(playlistChoiceBox.getValue());
        try {
            PlayList<Composition> playList = new PlayList<>();
            String query = "SELECT " + Handler.getPlaylist() + ".name, " +
                    Handler.getPlaylist() + ".artists, " +
                    Handler.getPlaylist() + ".albums, " +
                    Handler.getPlaylist() + ".id_track, " +
                    "uploaded_tracks.uuid_track " +
                    "FROM " + Handler.getPlaylist() + " " +
                    "JOIN uploaded_tracks ON " + Handler.getPlaylist() + ".id_track = uploaded_tracks.id_track;";
            ResultSet rs = DatabaseHandler.getResultSet(query);
            while (rs.next()) {
                playList.addHead(new Composition(rs.getString("name"),
                        rs.getString("artists"),
                        rs.getString("albums"),
                        rs.getString("uuid_track")));
            }
            if (playList.size() == 0) {
                Handler.throwInfoAlert("THERE ARE NO TRACKS", "Add at least one track to the playlist.");
                return;
            }
            setTracks(playList);
        } catch (SQLException e) {
            System.err.println("Could not set tracks: \n"
                    + e.getMessage());
        }
    }

    private List<String> getPlaylists () {
        try {
            List<String> playlists = new ArrayList<>();
            ResultSet rs = DatabaseHandler.getResultSet("SELECT * FROM playlists;");
            while (rs.next()) {
                playlists.add(rs.getString(1));
            }
            return playlists;
        } catch (SQLException e) {
            System.err.println("Could not get the playlists:\n"
            + e.getMessage());
        }
        return null;
    }

    private void openFileChooser() {
        if (Handler.getPlaylist() == null) {
            Handler.throwErrorAlert("SELECT THE PLAYLIST", "First select the playlist to add a track to.");
            return;
        }
        Stage stage = (Stage) mainAnchor.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add the track");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Audio Files", "*.mp3", "*.wav"));
        File file = fileChooser.showOpenDialog(stage);
        if (file.exists()) {
            openDialogueTrack(file);
        }
        else {
            Handler.throwErrorAlert("CHOOSING_FILE_ERROR",
                    "File not found.");
        }
    }

    private void openDialogueTrack (File file) {
        System.out.println("File found: " + file.getAbsolutePath() + "\n");
        Handler.setChosenFile(file);
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/dialogueTrack.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println("Could not loaded dialogue track fxml file."
            + e.getMessage());
        } finally {
            trackChoiceBox.getItems().clear();
            getTracks();
        }
    }

    private void openDialoguePlaylist () {
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/dialoguePlaylist.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println("Could not loaded dialogue playlist fxml file."
                    + e.getMessage());
        } finally {
            playlistChoiceBox.getItems().clear();
            playlistChoiceBox.getItems().addAll(getPlaylists());
        }
    }
}
