package classes;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/*
Класс-аппликация музыкального плеера
 */
public class MusicPlayer extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    /*
    Стандартный метод для запуска аппликации в виде окна с файла fxml
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/mainWindow.fxml")));
        primaryStage.setTitle("Music player(but now it's just window)");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
