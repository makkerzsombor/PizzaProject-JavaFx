package hu.pizza.pizzaproject.components;

import com.google.gson.Gson;
import hu.pizza.pizzaproject.Application;
import hu.pizza.pizzaproject.model.User;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.auth.JwtResponse;
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

/**
 * Belépéskontroller osztály.
 */
public class LoginController {
    /**
     * Ez a logonkat tartalmazó ImageView.
     */
    @FXML
    private ImageView logoView;
    /**
     * Ez az emailt tartalmazó TextField.
     */
    @FXML
    private TextField emailField;
    /**
     * Ez a jelszót tartalmazó TextField.
     */
    @FXML
    private PasswordField passwordField;
    /**
     * Gomb aminek lenyomásával betud lépni.
     */
    @FXML
    private Button loginButton;
    /**
     * Loginhoz szükséges link.
     */
    private static final String LOGIN_API_URL = "http://localhost:8080/auth";

    /**
     * Program futtatása után elinduló funkció, ami beteszi a logónkat az InageViewba.
     */
    @FXML
    private void initialize(){
        logoView.setImage(null);
        Image kepem = new Image("logoszoveg.png");
        logoView.setImage(kepem);
    }

    /**
     * Ez a függvény felelős a felugró hiba üzenetekért.
     * @param owner A felugró ablak elhelyezéért felelős Node.
     * @param title A felugró ablak címe.
     * @param message A felugró ablak üzenete.
     */
    private static void showAlert(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    /**
     * Belépés gomb lenyomásáért felelős funkció.
     */
    public void loginClick() {
        // üres Field ellenőrzés
        Window owner = loginButton.getScene().getWindow();
        if (emailField.getText().isEmpty()) {
            showAlert(owner, "Form Hiba!",
                    "Kérlek írd be az emailed!");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(owner, "Form Hiba!",
                    "Kérlek írd be a jelszavad!");
            return;
        }

        // Ez a token tovább küldött json file:
        String email = emailField.getText();
        String password = passwordField.getText();

        //User object létrehozása
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Http cliens létrehozása
        HttpClient httpClient = HttpClient.newHttpClient();

        // Loginrequest (POST)
        HttpRequest loginRequestPost;
        try {
            loginRequestPost = HttpRequest.newBuilder()
                    .uri(new URI(LOGIN_API_URL + "/admin-login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user)))
                    .build();

            HttpResponse<String> response = httpClient.send(loginRequestPost, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                JwtResponse jwtResponse = new Gson().fromJson(response.body(), JwtResponse.class);
                ApplicationConfiguration.setJwtResponse(jwtResponse);
                newAblak();
            }else if(response.statusCode() == 404){
                showAlert(owner, "Form Hiba!",
                        "Az email/jelszó párosod nem egyezik!");
            }else if(response.statusCode() == 403){
                showAlert(owner, "Belépési Hiba!",
                        "Nincsen admin jogod!");
            }else{
                System.out.println(response.statusCode());
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Fő ablakot megjelenítő funkció.
     */
    private void newAblak(){
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("homepage-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 1024, 768);
            scene.getStylesheets().add("style.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Homepage");
        stage.setScene(scene);
        stage.setResizable(false);
        Image icon = new Image("kesz_arany_logo.png");
        stage.getIcons().add(icon);
        stage.show();

        // Ablak bezárás
        Stage stagebezaras = (Stage) loginButton.getScene().getWindow();
        stagebezaras.close();
    }
}