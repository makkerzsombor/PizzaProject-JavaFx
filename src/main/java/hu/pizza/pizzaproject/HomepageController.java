package hu.pizza.pizzaproject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.Model.JwtToken;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    private String URL = "http://localhost:8080/user";

    @FXML
    private void initialize() {
        //TODO: Emberünk nevének kiírása

        // JWT token kiolvasás
        String jsonToken = ApplicationConfiguration.getJwtToken().getJwtToken();

        // User létre hozása
        User user = new User();

        // GSON converter
        Gson converter = new Gson();

        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;

        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL + "/data"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + jsonToken)
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response body into a User object using Gson
            System.out.println(response.body());
            user = converter.fromJson(response.body(), User.class);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }


        felsoNev.setText("Üdvözöljük kedves " + user.getFirst_name());
    }

    public void kilepesClick(ActionEvent actionEvent) {
        JwtToken jwttoken = new JwtToken();
        jwttoken.setJwtToken("");
        ApplicationConfiguration.setJwtToken(jwttoken);

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

    public void pizzaCreate(ActionEvent actionEvent) {
        //TODO: Pizza létrehozás form
    }

    public void userListing(ActionEvent actionEvent) {
        adatokBoxClear();

        // Tábla cím
        Text text = new Text();
        text.setText("User adatok");
        adatokBox.setMargin(text, new Insets(0, 0, 10, 0));
        text.setStyle("-fx-fill: white;");
        text.setFont(Font.font(15));
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.TOP_CENTER);

        // TableView hozzáadás
        TableView lista = new TableView();

        // id
        TableColumn<User ,Integer> column1 =
                new TableColumn<>("Id");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Id"));

        // email
        TableColumn<User ,String> column2 =
                new TableColumn<>("Email");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("Email"));

        // lastname
        TableColumn<User ,String> column3 =
                new TableColumn<>("Lastname");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("last_name"));

        // firstname
        TableColumn<User ,String> column4 =
                new TableColumn<>("Firstname");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("first_name"));

        // password
        TableColumn<User ,String> column5 =
                new TableColumn<>("Password");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("password"));

        // admin
        TableColumn<User ,Boolean> column6 =
                new TableColumn<>("Admin");

        column6.setCellValueFactory(
                new PropertyValueFactory<>("admin"));

        lista.getColumns().add(column1);
        lista.getColumns().add(column2);
        lista.getColumns().add(column3);
        lista.getColumns().add(column4);
        lista.getColumns().add(column5);
        lista.getColumns().add(column6);

        adatokBox.getChildren().add(lista);

        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // HTTP Request
        HttpRequest usersrequest = null;

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            // Prepare the request
            usersrequest = HttpRequest.newBuilder()
                    .uri(new URI(URL + "/get-all"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(usersrequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response body into a List<User> object using Gson
            Type userListType = new TypeToken<List<User>>(){}.getType();
            userLista = converter.fromJson(response.body(), userListType);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }

        /*// console kilistázás
        for (User item : userLista) {
            System.out.println(item.getId() + " " + item.getEmail() + " " + item.getLast_name() + " " + item.getFirst_name() + " " + item.getPassword());
        }*/

        // Listából tableView
        lista.setItems(FXCollections.observableArrayList(userLista));

    }
}
