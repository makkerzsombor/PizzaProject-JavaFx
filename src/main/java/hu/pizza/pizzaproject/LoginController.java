package hu.pizza.pizzaproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LoginController {

    @FXML
    private ImageView logoView;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private static final String LOGIN_API_URL = "http://localhost:8080/";

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
        //TODO: Ellenörzés

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
        // URL:
        String baseUrl = "http://localhost:8080/user";

        // Ez a token tovább küldött json file:
        String email = emailField.getText();
        String password = passwordField.getText();
        String json = "{ \"email\" :" + "\"" + email + "\","
                + "\"password\"" + ": \"" + password + "\" }";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        //new gson to json
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // ezt adom a bodypublishernek lejebb

        HttpClient httpClient = HttpClient.newHttpClient();

        // Loginrequest (POST)
        HttpRequest loginRequest = null;
        try {
            System.out.println(HttpRequest.BodyPublishers.ofString(json));
            loginRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(loginRequest.headers() + "\n" + loginRequest.uri());

            if (response.statusCode() == 200){
                System.out.println("Sikeres token kreálás");
                System.out.println();
            }else if(response.statusCode() == 400){
                System.out.println("rossz syntax / request");
            }else if(response.statusCode() == 404){
                System.out.println("Not found");
            }else{
                System.out.println("Valami nem okés");
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }



        /*CompletableFuture<HttpResponse<String>> loginResponseFuture = httpClient.sendAsync(loginRequest, HttpResponse.BodyHandlers.ofString());
        String jwtToken = null;
        try {
            jwtToken = loginResponseFuture.thenApply(HttpResponse::body).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Send a GET request to the /data endpoint with the JWT token to get the user information
        HttpRequest dataRequest = null;
        try {
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/data"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();
            System.out.println(jwtToken);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<HttpResponse<String>> dataResponseFuture = httpClient.sendAsync(dataRequest, HttpResponse.BodyHandlers.ofString());
        String userDataJson = null;
        try {
            userDataJson = dataResponseFuture.thenApply(HttpResponse::body).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Print the user information
        System.out.println(userDataJson);*/

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