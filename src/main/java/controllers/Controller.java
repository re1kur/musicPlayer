package controllers;

import classes.Composition;
import classes.Node;
import classes.PlayList;
import handlers.DatabaseHandler;
import handlers.FileStorageHandler;
import handlers.Handler;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/*
Класс-контроллер окна mainWindow для обработки нажатий на разные кнопки
 */
public class Controller {
    private boolean isPlaying = false;

    private PlayList<Composition> currentPlaylist;

    private File audioFile;

    private MediaPlayer mp;

    @FXML private Button addPlaylistBtn;

    @FXML private Label artistsAlbumsLabel;

    @FXML private Button closeWindowBtn;

    @FXML private Button deleteSelectedPlaylistBtn;

    @FXML private Button deleteSelectedTrackBtn;

    @FXML private AnchorPane mainAnchor;

    @FXML private Button nextTrackBtn;

    @FXML private Button openFileChooserBtn;

    @FXML private Button pauseOrPlayTrackBtn;

    @FXML private ChoiceBox<String> playlistChoiceBox;

    @FXML private Button prevTrackBtn;

    @FXML private ChoiceBox<Node<Composition>> trackChoiceBox;

    @FXML private Label trackNameLabel;

    @FXML private Button editSelectedTrackBtn;

    @FXML private Button changePosTrackBtn;
    /*
    Стандартный метод Javafx для определения методов для контролов при инициализации
    аппликации.
     */
    @FXML
    void initialize() {
        addPlaylistBtn.setOnAction(_ -> openDialoguePlaylist());
        playlistChoiceBox.getItems().addAll(getPlaylists());
        playlistChoiceBox.setOnAction(_ -> getTracks());
        closeWindowBtn.setOnAction(_ -> System.exit(0));
        openFileChooserBtn.setOnAction(_ -> openFileChooser());
        trackChoiceBox.setOnAction(_ -> selectTrack());
        pauseOrPlayTrackBtn.setOnAction(_ -> playSelectedTrack());
        prevTrackBtn.setOnAction(_ -> selectPrevTrack());
        nextTrackBtn.setOnAction(_ -> selectNextTrack());
        deleteSelectedPlaylistBtn.setOnAction(_ -> deletePlaylist());
        deleteSelectedTrackBtn.setOnAction(_ -> deleteTrack());
        editSelectedTrackBtn.setOnAction(_ -> editTrack());
        changePosTrackBtn.setOnAction(_ -> changePositionTrack());
    }
    /*
    Метод для изменения позиции трека в плейлисте
     */
    private void changePositionTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert("SELECT THE TRACK",
                    "Could not change position of a track. First, select a track.");
            return;
        }
        Handler.setSelectedNode(
                trackChoiceBox.getSelectionModel().getSelectedItem());
        openDialogueChangePos();
    }
    /*
    Метод для открытия модального окна-диалога для изменения позиции трека
     */
    private void openDialogueChangePos() {
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root =
                    FXMLLoader.load(getClass().getResource("/dialogueChangePos.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println(
                    "Could not loaded dialogue to change the position fxml file."
                            + e.getMessage());
        } finally {
            dialogue.close();
            clearTrackControls();
            setTracks(Handler.getPlaylist());
            trackChoiceBox.getSelectionModel().select(currentPlaylist.getCurrent());
        }
    }
    /*
    Метод для редактирования трека
     */
    private void editTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert(
                    "SELECT THE TRACK", "Could not edit a track. First, select a track.");
            return;
        }
        Handler.setSelectedNode(
                trackChoiceBox.getSelectionModel().getSelectedItem());
        openDialogueEdit();
    }

    /*
    Метод для открытия модального окна-диалога для редактирования трека
     */
    private void openDialogueEdit() {
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root =
                    FXMLLoader.load(getClass().getResource("/dialogueEdit.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println(
                    "Could not loaded dialogue edit fxml file." + e.getMessage());
        } finally {
            dialogue.close();
            clearTrackControls();
            getTracks();
            trackChoiceBox.getSelectionModel().select(currentPlaylist.getCurrent());
        }
    }
    /*
    Метод для удаления выбранного трека
     */
    private void deleteTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert("SELECT THE TRACK",
                    "Could not delete a track. First, select a track.");
            return;
        }
        try {
            currentPlaylist.deleteCurrent();
            DatabaseHandler.deleteTrack(
                    playlistChoiceBox.getSelectionModel().getSelectedItem(),
                    trackChoiceBox.getSelectionModel().getSelectedItem().getValue());
        } finally {
            clearTrackControls();
            getTracks();
            trackChoiceBox.getSelectionModel().select(currentPlaylist.getCurrent());
        }
    }
    /*
    Метод для удаления выбранного плейлиста
     */
    private void deletePlaylist() {
        if (playlistChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert("SELECT THE PLAYLIST",
                    "Could not delete a playlist. First, select a playlist.");
            return;
        }
        try {
            DatabaseHandler.deletePlaylist(
                    playlistChoiceBox.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            System.err.println("Could not delete playlist "
                    + playlistChoiceBox.getSelectionModel().getSelectedItem() + ":\n"
                    + e.getMessage());
        } finally {
            clearPlaylistControls();
            playlistChoiceBox.getItems().addAll(getPlaylists());
        }
    }
    /*
    Метод для переключения трека с текущего на следующий
     */
    private void selectNextTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert("SELECT THE TRACK",
                    "Could not turn next track. First, select a track.");
            return;
        }
        boolean isPlayed = false;
        if (isPlaying) {
            mp.stop();
            isPlayed = true;
        }
        Node<Composition> next = currentPlaylist.getCurrent().getNextNode();
        trackChoiceBox.getSelectionModel().select(next);
        selectTrack();
        if (isPlayed) {
            playSelectedTrack();
        }
    }
    /*
    Метод для переключения текущего трека на предыдущий
     */
    private void selectPrevTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert("SELECT THE TRACK",
                    "Could not turn previous track. First, select a track.");
            return;
        }
        boolean isPlayed = false;
        if (isPlaying) {
            mp.stop();
            isPlayed = true;
        }
        Node<Composition> prev = currentPlaylist.getCurrent().getPreNode();
        trackChoiceBox.getSelectionModel().select(prev);
        selectTrack();
        if (isPlayed) {
            playSelectedTrack();
        }
    }
    /*
    Метод для проигрывания выбранного трека
     */
    private void playSelectedTrack() {
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Handler.throwErrorAlert(
                    "SELECT THE TRACK", "Could not play a track. First, select a track.");
            return;
        }
        mp.play();
        isPlaying = true;
        pauseOrPlayTrackBtn.setText("| |");
        pauseOrPlayTrackBtn.setOnAction(_ -> pauseSelectedTrack());
    }
    /*
    Метод для установки паузы для выбранного трека
     */
    private void pauseSelectedTrack() {
        mp.pause();
        isPlaying = false;
        pauseOrPlayTrackBtn.setText("⯈");
        pauseOrPlayTrackBtn.setOnAction(_ -> playSelectedTrack());
    }
    /*
    Метод для выбора трека
     */
    private void selectTrack() {
        if (isPlaying) {
            mp.stop();
            isPlaying = false;
            pauseOrPlayTrackBtn.setText("⯈");
            pauseOrPlayTrackBtn.setOnAction(_ -> playSelectedTrack());
        }
        if (trackChoiceBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        Node<Composition> selectedNode =
                trackChoiceBox.getSelectionModel().getSelectedItem();
        currentPlaylist.setCurrent(selectedNode);
        Composition selected = selectedNode.getValue();
        trackNameLabel.setText(selected.getName());
        artistsAlbumsLabel.setText(
                selected.getArtists() + " | " + selected.getAlbums());
        audioFile = FileStorageHandler.downloadTrack(selected.getUuid());
        if (audioFile.exists()) {
            System.out.println(
                    "Temp. file " + audioFile.getAbsolutePath() + " exists.");
        }
        mp = new MediaPlayer(new Media(audioFile.toURI().toString()));
    }
    /*
    Метод-сеттер для choiceBox
     */
    private void setTracks(PlayList<Composition> playList) {
        playList.setCurrent(playList.getTail());
        while (playList.getCurrent().getPreNode() != playList.getTail()) {
            trackChoiceBox.getItems().addFirst(playList.getCurrent());
            playList.turnLeftCurrent();
        }
        trackChoiceBox.getItems().addFirst(playList.getCurrent());
        playList.setCurrent(playList.getHead());
    }
    /*
    Метод-геттер треков из бд
     */
    private void getTracks() {
        if (playlistChoiceBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        clearTrackControls();
        Handler.setPlaylistName(
                playlistChoiceBox.getSelectionModel().getSelectedItem());
        try {
            currentPlaylist = new PlayList<>();
            String query = "SELECT " + Handler.getPlaylistName() + ".id, "
                    + Handler.getPlaylistName() + ".name, " + Handler.getPlaylistName()
                    + ".artists, " + Handler.getPlaylistName() + ".albums, "
                    + Handler.getPlaylistName() + ".id_track, "
                    + "uploaded_tracks.uuid_track "
                    + "FROM " + Handler.getPlaylistName() + " "
                    + "JOIN uploaded_tracks ON " + Handler.getPlaylistName()
                    + ".id_track = uploaded_tracks.id_track"
                    + " ORDER BY id ASC;";
            ResultSet rs = DatabaseHandler.getResultSet(query);
            while (rs.next()) {
                currentPlaylist.addHead(new Composition(rs.getInt("id"),
                        rs.getString("name"), rs.getString("artists"),
                        rs.getString("albums"), rs.getString("uuid_track")));
            }
            if (currentPlaylist.size() == 0) {
                Handler.throwInfoAlert(
                        "THERE ARE NO TRACKS", "Add at least one track to the playlist.");
                return;
            }
            Handler.setPlaylist(currentPlaylist);
            setTracks(currentPlaylist);
        } catch (SQLException e) {
            System.err.println("Could not set tracks: \n" + e.getMessage());
        }
    }
    /*
    Метод для получения всех плейлистов из бд
     */
    private List<String> getPlaylists() {
        try {
            List<String> playlists = new ArrayList<>();
            ResultSet rs = DatabaseHandler.getResultSet("SELECT * FROM playlists;");
            while (rs.next()) {
                playlists.add(rs.getString(1));
            }
            return playlists;
        } catch (SQLException e) {
            System.err.println("Could not get the playlists:\n" + e.getMessage());
        }
        return null;
    }
    /*
    Метод для открытия модального окна-диалога для выбора трека для загрузки его в плейлист/файловое хранилище
     */
    private void openFileChooser() {
        if (Handler.getPlaylistName() == null) {
            Handler.throwErrorAlert("SELECT THE PLAYLIST",
                    "First select the playlist to add a track to.");
            return;
        }
        Stage stage = (Stage) mainAnchor.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add the track");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        if (file.exists()) {
            openDialogueTrack(file);
        } else {
            Handler.throwErrorAlert("CHOOSING_FILE_ERROR", "File not found.");
        }
    }
    /*
    Метод для открытия модального окна-диалога для вставки выбранного трека в плейлист
     */
    private void openDialogueTrack(File file) {
        System.out.println("File found: " + file.getAbsolutePath() + "\n");
        Handler.setChosenFile(file);
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root =
                    FXMLLoader.load(getClass().getResource("/dialogueTrack.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println(
                    "Could not loaded dialogue track fxml file." + e.getMessage());
        } finally {
            dialogue.close();
            trackChoiceBox.getItems().clear();
            getTracks();
            trackChoiceBox.getSelectionModel().select(currentPlaylist.getCurrent());
        }
    }
    /*
    Метод для открытия модального окна-диалога для создания плейлиста
     */
    private void openDialoguePlaylist() {
        Stage dialogue = new Stage();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initModality(Modality.WINDOW_MODAL);
        dialogue.initOwner(mainAnchor.getScene().getWindow());
        try {
            Parent root =
                    FXMLLoader.load(getClass().getResource("/dialoguePlaylist.fxml"));
            dialogue.setScene(new Scene(root));
            dialogue.showAndWait();
        } catch (IOException e) {
            System.err.println(
                    "Could not loaded dialogue playlist fxml file." + e.getMessage());
        } finally {
            dialogue.close();
            playlistChoiceBox.getItems().clear();
            playlistChoiceBox.getItems().addAll(getPlaylists());
        }
    }
    /*
    Метод для очистки контролов, связыных с треком
     */
    private void clearTrackControls() {
        trackChoiceBox.getSelectionModel().clearSelection();
        trackChoiceBox.getItems().clear();
        trackNameLabel.setText("");
        artistsAlbumsLabel.setText("");
        if (isPlaying) {
            mp.stop();
            isPlaying = false;
        }
        audioFile = null;
    }
    /*
    Метод для очистки всех контролов, включая трек и плейлист
     */
    private void clearPlaylistControls() {
        clearTrackControls();
        playlistChoiceBox.getSelectionModel().clearSelection();
        playlistChoiceBox.getItems().clear();
        currentPlaylist = null;
    }
}
