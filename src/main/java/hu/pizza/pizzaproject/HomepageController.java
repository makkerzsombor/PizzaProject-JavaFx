package hu.pizza.pizzaproject;

import hu.pizza.pizzaproject.Dtos.PizzaDto;
import hu.pizza.pizzaproject.Dtos.UserDto;
import hu.pizza.pizzaproject.FormsAndLists.FormsAndLists;
import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.Model.JwtToken;
import hu.pizza.pizzaproject.DataClasses.Pizza;
import hu.pizza.pizzaproject.DataClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.http.HttpResponse;

public class HomepageController {
    @FXML
    private VBox adatokBox = new VBox();
    @FXML
    private Button kilepesButton;
    @FXML
    private Label felsoNev;
    @FXML
    private TableView<User> userLista = new TableView<>();
    @FXML
    private TableView<Pizza> pizzaLista = new TableView<>();
    private String USER_URL = "http://localhost:8080/user";
    private String PIZZA_URL = "http://localhost:8080/pizza";
    private String ORDER_URL = "http://localhost:8080/order";
    private RequestHandler requestHandler = new RequestHandler();
    private boolean userTable;

    @FXML
    private void initialize() {
        User user = requestHandler.getUserRequest(USER_URL);
        // Felső labelbe név rakás
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

    public void modositasClick(ActionEvent actionEvent) {
        if (userTable) {
            int selectedIndex = userLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            } else {
                // adatok mentése
                User selected = userLista.getSelectionModel().getSelectedItem();
                User modifyingUser = new User(selected.getId(), selected.getFirst_name(), selected.getLast_name(), selected.getEmail(), selected.getPassword(), selected.isAdmin());
                userModositasFormCreate(modifyingUser);
            }
        } else {
            int selectedIndex = pizzaLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            } else {
                Pizza selected = pizzaLista.getSelectionModel().getSelectedItem();
                Pizza modifyingPizza = new Pizza(selected.getId(), selected.getName(), selected.getPicture(), selected.getDescription(), selected.getPrice());
                pizzaModositasFormCreate(modifyingPizza);
            }
        }
    }

    private void pizzaModositasFormCreate(Pizza modifyingPizza) {
        // FormsandLists
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        PizzaDto pizzaDto = formsAndLists.pizzaUpdateForm(modifyingPizza);
        //kiolvasom egy változoba
        adatokBox.getChildren().add(pizzaDto.getVbox());
        // Gomb kreálás
        Button mentesButton = new Button("Mentés");
        adatokBox.getChildren().add(mentesButton);

        mentesButton.setOnAction((event) -> {
            //dtobol a pizza adatok
            int updateId = modifyingPizza.getId();
            Pizza readyPizza = new Pizza(updateId, pizzaDto.getName().getText(), pizzaDto.getPicture().getText(), pizzaDto.getDescription().getText(), pizzaDto.getPrice().getValue());
            pizzaModositasFelmasolas(readyPizza, updateId);
        });
    }

    private void pizzaModositasFelmasolas(Pizza readyPizza, int updateId) {
        // Modositas
        HttpResponse response = requestHandler.updatePizzaRequest(readyPizza, updateId, PIZZA_URL);
        if (response.statusCode() == 200) {
            Window window = adatokBox.getScene().getWindow();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres módosítás", "Az adatbázist sikeresen frissitettük");
        } else {
            System.out.println(response.body());
            System.out.println("Valami rossz");
        }
        // Táblázat újra generálása
        pizzaListCreate();
    }

    private void userModositasFormCreate(User modifyingUser) {
        // FormsandLists
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        UserDto userDto = formsAndLists.userUpdateForm(modifyingUser);
        //kiolvasom egy változoba
        adatokBox.getChildren().add(userDto.getVbox());
        // Gomb kreálás
        Button mentesButton = new Button("Mentés");
        adatokBox.getChildren().add(mentesButton);

        mentesButton.setOnAction((event) -> {
            //dtobol a pizza adatok
            long updateId = modifyingUser.getId();
            // jelszó megadás ellenőrzése
            if (userDto.getPassword().getText() == null) {
                User noChangePassWordUser = new User(updateId, userDto.getFirst_name().getText(), userDto.getLast_name().getText(), userDto.getEmail().getText(), userDto.getAdmin().isSelected());
                userModositasFelmasolas(noChangePassWordUser,updateId);
            } else {
                User changePasswordUser = new User(updateId, userDto.getFirst_name().getText(), userDto.getLast_name().getText(), userDto.getEmail().getText(), userDto.getPassword().getText(),userDto.getAdmin().isSelected());
                userModositasFelmasolas(changePasswordUser,updateId);
            }
        });
    }

    private void userModositasFelmasolas(User readyUser, long updateId) {
        // Modositas
        HttpResponse response = requestHandler.updateUserRequest(readyUser, updateId, USER_URL);
        if (response.statusCode() == 200) {
            Window window = adatokBox.getScene().getWindow();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres módosítás", "Az adatbázist sikeresen frissitettük");
        } else {
            System.out.println(response.body());
            System.out.println("Valami rossz");
        }
        // Táblázat újra generálása
        userListCreate();
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void torlesClick(ActionEvent actionEvent) {
        if (userTable) {
            // Kijelölés ellenőrzése
            User selected = userLista.getSelectionModel().getSelectedItem();
            long selectedIndex = selected.getId();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy felhasználot!");
            } else {
                megerositoAlert(Alert.AlertType.ERROR, owner, "Biztos!", "Biztosan törölni akarja felhasználot?", selectedIndex);
            }
        } else if (!userTable) {
            Pizza selected = pizzaLista.getSelectionModel().getSelectedItem();
            long selectedIndex = selected.getId();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy pizzát!");
            } else {
                megerositoAlert(Alert.AlertType.ERROR, owner, "Biztos!", "Biztosan törölni akarja a kijelölt pizzát?", selectedIndex);
            }
        } else {
            Window owner = kilepesButton.getScene().getWindow();
            showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Nem jelölt ki elemet!");
        }

    }

    private void megerositoAlert(Alert.AlertType alertType, Window owner, String title, String message, long selectedIndex) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        // Button-ok
        ButtonType okButton = new ButtonType("Igen");
        ButtonType cancelButton = new ButtonType("Mégsem", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Vizsgáljuk, hogy melyik táblázatból jelöltünk ke elemet
        if (userTable) {
            // User
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    userVeglegesTorles(selectedIndex);
                }
            });
        } else {
            // Pizza
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    pizzaVeglegesTorles(selectedIndex);
                }
            });
        }

    }

    private void userVeglegesTorles(long selectedIndex) {
        // Törlés
        HttpResponse response = requestHandler.deleteRequest(USER_URL, selectedIndex);
        if (response.statusCode() == 200) {
            Window window = adatokBox.getScene().getWindow();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres Törlés", "Az adott felhasználót sikeresen eltávolítottuk");
        } else {
            System.out.println(response.body());
            System.out.println("Valami rossz");
        }
        // Táblázat ujra kreálása:
        userListCreate();
    }
    private void pizzaVeglegesTorles(long selectedIndex) {
        // Törlés
        HttpResponse response = requestHandler.deleteRequest(PIZZA_URL, selectedIndex);
        if (response.statusCode() == 200) {
            Window window = adatokBox.getScene().getWindow();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres Törlés", "Az adott pizzát sikeresen eltávolítottuk");
        } else {
            System.out.println(response.body());
            System.out.println("Valami rossz");
        }
        // Táblázat ujra kreálása:
        pizzaListCreate();
    }
    private void adatokBoxClear() {
        adatokBox.getChildren().clear();
    }

    public void pizzaListing(ActionEvent actionEvent) {
        pizzaListCreate();
    }

    public void pizzaCreate(ActionEvent actionEvent) {
        addPizzaFormCreate();
    }

    private void addPizzaFormCreate() {
        adatokBoxClear();
        //Létrehozott formandlist
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createPizza(kilepesButton, pizzaLista));
    }

    private void userListCreate() {
        userTable = true;
        adatokBoxClear();
        userLista.getItems().clear();
        userLista.getColumns().clear();
        //Létrehozott formandlist
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createUserList(userLista));
    }

    private void pizzaListCreate() {
        userTable = false;
        adatokBoxClear();
        pizzaLista.getItems().clear();
        pizzaLista.getColumns().clear();
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createPizzaList(pizzaLista));
    }

    public void userListing(ActionEvent actionEvent) {
        userListCreate();
    }

    public void rendelesClick(ActionEvent actionEvent) {
        rendelesFormFrissites();
    }

    public void rendelesFormFrissites() {
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        adatokBox.setAlignment(Pos.TOP_CENTER);
        adatokBox.getChildren().add(formsAndLists.orderListCreate(ORDER_URL));
    }
}