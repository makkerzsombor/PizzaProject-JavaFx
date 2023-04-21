package hu.pizza.pizzaproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    /**
     * Ez a funkció indítja el a programunkat.
     * @param stage Ablakon belüli GUI elem, amire megjelenik a programunk.
     * @throws IOException Amennyiben nem sikerült a stage-et beolvasnia IOException-t dob.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        stage.setTitle("Pizza Váltó");
        stage.setScene(scene);
        Image icon = new Image("kesz_arany_logo.png");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        scene.getStylesheets().add("style.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}