package hu.pizza.pizzaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    private void initialize() {
        //TODO: Név betöltés jobb fentre.
    }




    public void menuItemPizzaAdatok(ActionEvent actionEvent) {
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

        //TODO: Pizzák lekérése backendből
        Pizza pizza = new Pizza(1, "sajtos", "sajtospizza.png", "Finom sajtos pizza", 4.5, 2500);

        lista.getItems().add(pizza);
        lista.setMinHeight(630);
        text.setFont(Font.font(15));
        adatokBox.getChildren().add(lista);
    }

    public void menuItemFelhasznalok(ActionEvent actionEvent) {
        adatokBoxClear();

        // Tábla cím
        Text text = new Text();
        text.setText("Felhasználói adatok");
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.CENTER);

        // TableView Hozzáadása
        TableView lista = new TableView();

        // Id
        TableColumn<String ,Integer> column1 =
                new TableColumn<>("Id");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Id"));

        // Email
        TableColumn<String ,String> column2 =
                new TableColumn<>("Email");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("Email"));

        // Password
        TableColumn<String ,String> column3 =
                new TableColumn<>("Password");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("Password"));

        // Admin
        TableColumn<String ,Boolean> column4=
                new TableColumn<>("Admin");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("Admin"));

        // FirstName
        TableColumn<String ,String> column5 =
                new TableColumn<>("First Name");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("first_name"));

        // LastName
        TableColumn<String ,String> column6 =
                new TableColumn<>("Last Name");

        column6.setCellValueFactory(
                new PropertyValueFactory<>("last_name"));


        lista.getColumns().add(column1);
        lista.getColumns().add(column2);
        lista.getColumns().add(column3);
        lista.getColumns().add(column4);
        lista.getColumns().add(column5);
        lista.getColumns().add(column6);

        //TODO:  Userek lekérése backendből
        User user = new User(1, "example@gmail.com", "Ez egy titkosított jelszó", true, "Makker", "Zsombor");

        lista.getItems().add(user);
        lista.setMinHeight(630);
        text.setFont(Font.font(15));
        adatokBox.getChildren().add(lista);
    }

    public void menuItemStatusz(ActionEvent actionEvent) {
        //TODO: Státusz még megbeszélésre szorul
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
}
