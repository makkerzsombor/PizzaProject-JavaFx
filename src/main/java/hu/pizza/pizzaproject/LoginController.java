package hu.pizza.pizzaproject;

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

public class LoginController {

    @FXML
    private ImageView logoView;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

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