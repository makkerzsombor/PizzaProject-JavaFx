package hu.pizza.pizzaproject;

import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.Model.JwtToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class HomepageController {

    @FXML
    private TextField keresoField;
    @FXML
    private VBox adatokBox;
    @FXML
    private Button kilepesButton;
    @FXML
    private Label felsoNev;
    @FXML
    private VBox ablak;

    @FXML
    private void initialize() {
        //TODO: token fogadása.
        System.out.printf(ApplicationConfiguration.getJwtToken().getJwtToken());
        JwtToken jwttoken = new JwtToken();
        jwttoken.setJwtToken("");
        ApplicationConfiguration.setJwtToken(jwttoken);
    }

    public void kilepesClick(ActionEvent actionEvent) {
        //TODO: Esetleges token eldobása

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
        scene.getStylesheets().add("style.css");
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

    private void adatokBoxClear(){
        adatokBox.getChildren().clear();
    }


    public void pizzaListing(ActionEvent actionEvent) {
        //TODO: Pizza kilistázás
    }

    public void pizzaChanges(ActionEvent actionEvent) {
        //TODO: Pizza módosítás form
    }

    public void pizzaCreate(ActionEvent actionEvent) {
        //TODO: Pizza létrehozás form
    }

    public void userListing(ActionEvent actionEvent) {
        adatokBoxClear();

        // Tábla cím
        Text text = new Text();
        text.setText("Pizza adatok");
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.CENTER);

        // TableView hozzáadás
        TableView lista = new TableView();

        // id
        TableColumn<String ,Integer> column1 =
                new TableColumn<>("Id");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Id"));

        // pizza_name
        TableColumn<String ,String> column2 =
                new TableColumn<>("Pizza Name");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("Pizza_name"));

        // picture
        TableColumn<String ,String> column3 =
                new TableColumn<>("Picture");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("Picture"));

        // description
        TableColumn<String ,String> column4 =
                new TableColumn<>("Description");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("Description"));

        // rating
        TableColumn<String ,Float> column5 =
                new TableColumn<>("Rating");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("Rating"));

        // price
        TableColumn<String ,Float> column6 =
                new TableColumn<>("Price");

        column6.setCellValueFactory(
                new PropertyValueFactory<>("Price"));

        lista.getColumns().add(column1);
        lista.getColumns().add(column2);
        lista.getColumns().add(column3);
        lista.getColumns().add(column4);
        lista.getColumns().add(column5);
        lista.getColumns().add(column6);

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // HTTP Request
        HttpRequest usersrequest = null;

        //TODO: userRequest

    }
}
