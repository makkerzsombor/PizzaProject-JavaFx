package hu.pizza.pizzaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

    @FXML
    private TextField keresoField;
    @FXML
    private VBox adatokBox;
    @FXML
    private Button kilepesButton;

    @FXML
    private void initialize(){
        //TODO: Név betöltés jobb fentre.
    }

    public void menuItemPizzaAdatok(ActionEvent actionEvent) {
        //TODO: Pizza tábla megjelenítés
    }

    public void menuItemFelhasznalok(ActionEvent actionEvent) {
        //TODO: Felhasználók tábla megjelenítés
    }

    public void menuItemElozmenyek(ActionEvent actionEvent) {
        //TODO: Elözmények tábla megjelenítés
    }

    public void kilepesClick(ActionEvent actionEvent) {
        // Visszalépés a login windowra
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1024, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Pizza Váltó");
        stage.setScene(scene);
        LoginController controller = fxmlLoader.getController();
        stage.setResizable(false);
        Image icon = new Image("kesz_arany_logo.png");
        stage.getIcons().add(icon);
        stage.show();

        // Ablak bezárása
        Stage stagebezaras = (Stage) kilepesButton.getScene().getWindow();
        stagebezaras.close();
    }

    public void keresoClick(ActionEvent actionEvent) {
        //TODO: megkeresni a keresoField elemét a felhasználok táblázatból és az adatBox-ba rakja
    }

    public void modositasClick(ActionEvent actionEvent) {
        //TODO: ha kivan jelölve 1 pizza/ember adatBox-ba form létrehozás modosításhoz
    }

    public void torlesClick(ActionEvent actionEvent) {
        //TODO: pizza/ember törlése
    }
}
