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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private TableView<User> lista = new TableView<>();

    private int Update_id;

    private String USER_URL = "http://localhost:8080/user";

    @FXML
    private void initialize() {
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
                    .uri(new URI(USER_URL + "/data"))
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
        // Kijelölés ellenőrzése
        int selectedIndex = lista.getSelectionModel().getSelectedIndex();
        Window owner = kilepesButton.getScene().getWindow();
        if (selectedIndex == -1){
            showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!","Először jelöljön ki egy elemet!");
        }else{
            // adatok mentése
            User selected = lista.getSelectionModel().getSelectedItem();
            User modifyingUser = new User(selected.getId(), selected.getFirst_name(), selected.getLast_name(), selected.getEmail(), selected.getPassword(), selected.isAdmin());
            modositasFormCreate(modifyingUser);
        }
    }
    private void modositasFormCreate(User modifyingUser){
        // Sceneben form létrehozása (A keszButton kell, mert csak így lehet margint állítani)
        VBox kisablakVbox = new VBox(10);

        // Firtsname
        Label firstName = new Label();
        firstName.setText("Firstname:");
        TextField firstNameTextField = new TextField();
        HBox firstNameSor = new HBox(10, firstName, firstNameTextField);

        // Lastsname
        Label lastName = new Label();
        lastName.setText("Lastname:");
        TextField lastNameTextField = new TextField();
        HBox lastNameSor = new HBox(10, lastName, lastNameTextField);

        // Email
        Label email = new Label();
        email.setText("Email:");
        TextField emailTextField = new TextField();
        HBox emailSor = new HBox(10, email, emailTextField);

        // Password
        Label password = new Label();
        password.setText("Password:");
        TextField passwordTextField = new TextField();
        HBox passwordSor = new HBox(10, password, passwordTextField);

        // Admin
        Label admin = new Label();
        admin.setText("Admin:");
        CheckBox adminCheckbox = new CheckBox();
        HBox adminSor = new HBox(10, admin, adminCheckbox);
        HBox.setMargin(adminCheckbox, new Insets(0,133,0,0));

        // Button
        Button keszButton = new Button();
        HBox modositasSor = new HBox(keszButton);
        HBox.setMargin(keszButton, new Insets(0,120,10,0));

        // Inputokba value-k:
        firstNameTextField.setText(modifyingUser.getFirst_name());
        lastNameTextField.setText(modifyingUser.getLast_name());
        emailTextField.setText(modifyingUser.getEmail());
        passwordTextField.setText(modifyingUser.getPassword());
        keszButton.setText("Mentés");
        if (modifyingUser.isAdmin()){
            adminCheckbox.setSelected(true);
        }else{
            adminCheckbox.setSelected(false);
        }
        // sceneClearing
        adatokBoxClear();

        // kialakítás design:
        adatokBox.setAlignment(Pos.CENTER);
        kisablakVbox.setAlignment(Pos.TOP_CENTER);

        kisablakVbox.setPadding(new Insets(0, 320, 10, 0));
        kisablakVbox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        // Hboxok
        firstNameSor.setAlignment(Pos.TOP_RIGHT);
        lastNameSor.setAlignment(Pos.TOP_RIGHT);
        emailSor.setAlignment(Pos.TOP_RIGHT);
        passwordSor.setAlignment(Pos.TOP_RIGHT);
        adminSor.setAlignment(Pos.TOP_RIGHT);
        modositasSor.setAlignment(Pos.TOP_RIGHT);

        // labelek
        firstName.setPadding(new Insets(5, 0, 0, 0));
        lastName.setPadding(new Insets(5, 0, 0, 0));
        email.setPadding(new Insets(5, 0, 0, 0));
        password.setPadding(new Insets(5, 0, 0, 0));
        admin.setPadding(new Insets(0, 0, 0, 0));
        keszButton.setPadding(new Insets(0,20,0,20));

        // Sorok:
        firstNameSor.setPadding(new Insets(10, 0, 0, 0));
        lastNameSor.setPadding(new Insets(10, 0, 0, 0));
        emailSor.setPadding(new Insets(10, 0, 0, 0));
        passwordSor.setPadding(new Insets(10, 0, 0, 0));
        adminSor.setPadding(new Insets(10,0,10,0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(firstNameSor, lastNameSor, emailSor, passwordSor, adminSor, modositasSor);
        adatokBox.getChildren().add(kisablakVbox);

        keszButton.setOnAction((event) -> {
            System.out.println("Jelszó ellenörzés");
            long updateId = modifyingUser.getId();
            User readyUser = new User(updateId, firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getText(), adminCheckbox.isSelected());
            if (readyUser.getPassword().equals(modifyingUser.getPassword())){
                System.out.println("A jelszavak azonosak");
                User noPasswordUser = new User(updateId, firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), adminCheckbox.isSelected());
                modositasFelmasolas(noPasswordUser, updateId);
            }else{
                modositasFelmasolas(readyUser, updateId);
                System.out.println("Rossz a jelszó");
            }
        });
    }
    private void modositasFelmasolas(User readyUser, long updateId){
        // GSON converter
        Gson converter = new Gson();

        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;

        // Jsonba átalakítás
        String jsonUser = converter.toJson(readyUser);
        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/" + updateId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                System.out.println("Siker");
                Window window = adatokBox.getScene().getWindow();
                showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres módosítás","Az adatbázist sikeresen frissitettük");
            }else{
                System.out.println(response.body());
                System.out.println("Valami rossz");
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }
        userListCreate();
    }
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    public void torlesClick(ActionEvent actionEvent) {
        // Kijelölés ellenőrzése
        User selected = lista.getSelectionModel().getSelectedItem();
        long selectedIndex = selected.getId();
        Window owner = kilepesButton.getScene().getWindow();
        if (selectedIndex == -1){
            showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!","Először jelöljön ki egy elemet!");
        }else{
            megerositoAlert(Alert.AlertType.ERROR, owner, "Biztos!","Biztosan törölni akarja az elemet!", selectedIndex);
        }
    }
    private void megerositoAlert(Alert.AlertType alertType, Window owner, String title, String message, long selectedIndex){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        // Button-ok
        ButtonType okButton= new ButtonType("Igen");
        ButtonType cancelButton = new ButtonType("Mégsem", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                System.out.println("OK button clicked");
                veglegestorles(selectedIndex);
            } else if (buttonType == cancelButton) {
                System.out.println("Cancel button clicked");
            }
        });
    }
    private void veglegestorles(long selectedIndex){
        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;
        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/" + selectedIndex))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                System.out.println("Siker");
                Window window = adatokBox.getScene().getWindow();
                showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres Törlés","Az elemet sikeresen eltávolítottuk");
            }else{
                System.out.println(response.body());
                System.out.println("Valami rossz");
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        userListCreate();
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

    public void userListCreate(){
        adatokBoxClear();
        lista.getItems().clear();
        lista.getColumns().clear();

        // Tábla cím
        Text text = new Text();
        text.setText("User adatok");
        adatokBox.setMargin(text, new Insets(0, 0, 10, 0));
        text.setStyle("-fx-fill: white;");
        text.setFont(Font.font(15));
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.TOP_CENTER);

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

        lista.getColumns().addAll(column1,column2,column3,column4,column5,column6);

        adatokBox.getChildren().add(lista);

        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // Előző törlési kisérletek


        // HTTP Request
        HttpRequest usersrequest = null;

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            // Prepare the request
            usersrequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/get-all"))
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
        // Listából tableViewba rakás
        lista.setItems(FXCollections.observableArrayList(userLista));
    }

    public void userListing(ActionEvent actionEvent) {
        userListCreate();
    }
}
