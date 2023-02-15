package hu.pizza.pizzaproject;

import com.google.gson.Gson;
import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.Model.JwtToken;
import hu.pizza.pizzaproject.Model.LoginRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    @FXML
    private ImageView logoView;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private static final String LOGIN_API_URL = "http://localhost:8080/user";

    @FXML
    private void initialize(){
        logoView.setImage(null);
        Image kepem = new Image("kesz_arany_logo.png");
        logoView.setImage(kepem);
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void loginClick(ActionEvent actionEvent) {
        //TODO: Backendbe kell egy admin login is

        // üres Field ellenőrzés
        Window owner = loginButton.getScene().getWindow();
        if (emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email first!");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password first!");
            return;
        }

        // Ez a token tovább küldött json file:
        String email = emailField.getText();
        String password = passwordField.getText();

        // Login request léthezása
         LoginRequest loginRequest = new LoginRequest();
         loginRequest.setEmail(email);
         loginRequest.setPassword(password);

        //new gson to json
        Gson converter = new Gson();
        String toPublisher = (converter.toJson(loginRequest));

        // Http cliens létrehozása
        HttpClient httpClient = HttpClient.newHttpClient();

        // Loginrequest (POST)
        HttpRequest loginRequestPost = null;
        try {
            loginRequestPost = HttpRequest.newBuilder()
                    .uri(new URI(LOGIN_API_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(toPublisher))
                    .build();

            HttpResponse<String> response = httpClient.send(loginRequestPost, HttpResponse.BodyHandlers.ofString());

            System.out.println(loginRequestPost.headers() + "\n" + loginRequestPost.uri());

            if (response.statusCode() == 200){
                System.out.println("Sikeres token kreálás");
                ApplicationConfiguration.setJwtToken(converter.fromJson(response.body(), JwtToken.class));
            }else if(response.statusCode() == 400){
                System.out.println("rossz syntax / request");
                return;
            }else if(response.statusCode() == 404){
                System.out.println("Not found");
                return;
            }else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Your email/password is incorrect");
                return;
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Átlépés a másik windowra
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("homepage-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1024, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Homepage");
        stage.setScene(scene);
        HomepageController controller = fxmlLoader.getController();
        stage.setResizable(false);
        Image icon = new Image("kesz_arany_logo.png");
        stage.getIcons().add(icon);
        stage.show();

        // Ablak bezárás
        Stage stagebezaras = (Stage) loginButton.getScene().getWindow();
        stagebezaras.close();
    }
}