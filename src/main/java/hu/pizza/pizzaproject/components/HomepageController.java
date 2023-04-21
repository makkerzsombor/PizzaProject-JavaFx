package hu.pizza.pizzaproject.components;

import hu.pizza.pizzaproject.Application;
import hu.pizza.pizzaproject.model.*;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.requests.ImgurRequests;
import hu.pizza.pizzaproject.requests.PizzaRequests;
import hu.pizza.pizzaproject.requests.UserRequests;
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

/**
 * Homepagekontroller osztály.
 */
public class HomepageController {
    /**
     * Ez a VBox felelős a különféle Formok, listák és táblázatok megjelenítéséért.
     */
    @FXML
    private VBox adatokBox = new VBox();
    /**
     * Egy gomb, amivel kitudunk lépni.
     */
    @FXML
    private Button kilepesButton;
    /**
     * Egy Label, amiben a belépett felhasználó keresztneve szerepel.
     */
    @FXML
    private Label felsoNev;
    /**
     * Felhasználókat tartalmazó TableView.
     */
    @FXML
    private final TableView<User> userLista = new TableView<>();
    /**
     * Pizzákat tartalmazó TableView.
     */
    @FXML
    private final TableView<Pizza> pizzaLista = new TableView<>();
    /**
     * Felhasználók kezelésére használt link.
     */
    private final String USER_URL = "http://localhost:8080/user";
    /**
     * Felhasználókkal kapcsolatos kérésekre használt UserRequest osztály.
     */
    private final UserRequests userRequests = new UserRequests();
    /**
     * Pizzákkal kapcsolatos kérésekre használt PizzaRequest osztály.
     */
    private final PizzaRequests pizzaRequests = new PizzaRequests();
    /**
     * Ez a boolean mutatja meg, hogy éppen melyik TableView van megnyitva(usertáblázat = true, pizzatáblázat = false).
     */
    private boolean userTable;
    /**
     * Lefutáskor elinduló funkció,ami a felső labelbe berakja a belépett felhasználó keresztnevét.
     */
    @FXML
    private void initialize() {
        User user = userRequests.getUserInformation(USER_URL);
        // Felső labelbe név rakás
        felsoNev.setText("Üdvözöljük kedves " + user.getFirst_name());
        userLista.getStyleClass().add("userTable");
        pizzaLista.getStyleClass().add("pizzaTable");
    }

    /**
     * Kiléptett a belépés ablakra és törli a felhasználói Tokeneket.
     */
    public void kilepesClick() {
        ApplicationConfiguration.setJwtResponse(null);

        // Visszalépés a login windowra
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 1024, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Pizza Váltó");
        stage.setScene(scene);
        stage.setResizable(false);
        Image icon = new Image("kesz_arany_logo.png");
        scene.getStylesheets().add("style.css");
        stage.getIcons().add(icon);
        stage.show();

        // Ablak bezárása
        Stage stagebezaras = (Stage) kilepesButton.getScene().getWindow();
        stagebezaras.close();
    }

    /**
     * Leellőrzi, hogy melyik táblázat van megnyitva és alapján küldi módosítási form létrehozásához kérést.
     */
    public void modositasClick() {
        if (userTable) {
            int selectedIndex = userLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            } else {
                // adatok mentése
                User selected = userLista.getSelectionModel().getSelectedItem();
                User modifyingUser = new User(selected.getId(), selected.getFirst_name(), selected.getLast_name(), selected.getEmail(), selected.getPassword(), selected.getAdmin());
                userModositasFormCreate(modifyingUser);
            }
        } else {
            int selectedIndex = pizzaLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            } else {
                Pizza selected = pizzaLista.getSelectionModel().getSelectedItem();
                Pizza modifyingPizza = new Pizza(selected.getId(), selected.getName(), selected.getPrice(), selected.getDescription(), selected.getPicture(), selected.isAvailable());
                pizzaModositasFormCreate(modifyingPizza);
            }
        }
    }
    /**
     * Ez a funkció rakja bele az adatokVBox-ba a pizza módosítás formot.
     * @param modifyingPizza Kijelölt Pizza típusú pizza.
     */
    private void pizzaModositasFormCreate(Pizza modifyingPizza) {
        // FormsandLists
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        PizzaDto pizzaDto = formsAndLists.pizzaUpdateForm(modifyingPizza);
        // Button létrehozása
        Button mentesButton = new Button("Mentés");
        mentesButton.getStyleClass().add("defaultButton");

        HBox gombSor = new HBox(mentesButton);
        gombSor.setAlignment(Pos.TOP_CENTER);
        gombSor.setPadding(new Insets(0, 117, 0, 320));
        pizzaDto.getVbox().getChildren().add(gombSor);
        adatokBox.getChildren().add(pizzaDto.getVbox());

        mentesButton.setOnAction((event) -> {
            if (pizzaDto.getName().getText().trim().isEmpty() || pizzaDto.getPicture().getText().trim().isEmpty() || pizzaDto.getDescription().getText().trim().isEmpty() || pizzaDto.getPrice().getText().trim().isEmpty()) {
                Window window = mentesButton.getScene().getWindow();
                showAlert(Alert.AlertType.ERROR, window, "Hiba", "Minden mezőt ki kell tölteni");
            } else {
                //dtobol a pizza adatok
                Long updateId = modifyingPizza.getId();
                // Vizsgálni kell, hogy mi történt:
                System.out.println("Pizza link: " + pizzaDto.getPicture().getText() + " Ez a filepath: " + FilePathAsString.getFilePath());
                if (FilePathAsString.getFilePath().equals("") && modifyingPizza.getPicture().equals(pizzaDto.getPicture().getText())) {
                    // Nem változik a kép semmilyen módon (de lehet az alatta lévő meghívást használni, mivel ugyanazt a linket küldjük el)
                    System.out.println("Nem változik a kép");
                    Pizza readyPizza = new Pizza(updateId, pizzaDto.getName().getText(), Integer.parseInt(pizzaDto.getPrice().getText()), pizzaDto.getDescription().getText(), pizzaDto.getPicture().getText(), pizzaDto.getAvailable().isSelected());
                    pizzaModositasFelmasolas(readyPizza, updateId);
                }
                if (FilePathAsString.getFilePath().equals("") && !modifyingPizza.getPicture().equals(pizzaDto.getPicture().getText())) {
                    // Más lett a kép link
                    System.out.println("Új link: " + pizzaDto.getPicture().getText());
                    Pizza readyPizza = new Pizza(updateId, pizzaDto.getName().getText(), Integer.parseInt(pizzaDto.getPrice().getText()), pizzaDto.getDescription().getText(), pizzaDto.getPicture().getText(), pizzaDto.getAvailable().isSelected());
                    pizzaModositasFelmasolas(readyPizza, updateId);
                }
                if (!FilePathAsString.getFilePath().equals("") && modifyingPizza.getPicture().equals(pizzaDto.getPicture().getText())) {
                    // Új képet kell feltölteni
                    System.out.println("Új kép path-je: " + FilePathAsString.getFilePath());
                    ImgurRequests imgurRequests = new ImgurRequests();
                    Pizza readyPizza = new Pizza(updateId, pizzaDto.getName().getText(), Integer.parseInt(pizzaDto.getPrice().getText()), pizzaDto.getDescription().getText(), imgurRequests.postImageToImgur(), pizzaDto.getAvailable().isSelected());
                    pizzaModositasFelmasolas(readyPizza, updateId);
                }
                if (!FilePathAsString.getFilePath().equals("") && !modifyingPizza.getPicture().equals(pizzaDto.getPicture().getText())) {
                    // Ilyen nem lehet mert egyszerre változna a link és a kép feltöltés is
                    Window window = kilepesButton.getScene().getWindow();
                    showAlert(Alert.AlertType.WARNING, window, "Nem jó használat", "Nem lehet egyszerre linket változtatni és képet feltölteni");
                    // Újra meghívás
                    pizzaModositasFormCreate(modifyingPizza);
                }
            }
        });
    }

    /**
     * Ez a funkció tölti fel a már módosított pizza adatait azt adatbázisba.
     * @param readyPizza Módosítandó pizza adatai.
     * @param updateId Módosítandó pizza id-je
     */
    private void pizzaModositasFelmasolas(Pizza readyPizza, Long updateId) {
        // Modositas
        String PIZZA_URL = "http://localhost:8080/pizza";
        HttpResponse<String> response = pizzaRequests.updatePizzaRequest(readyPizza, updateId, PIZZA_URL);
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

    /**
     * Ez a funkció rakja bele az adatokVBox-ba a felhasználó módosítás formot.
     * @param modifyingUser Kijelölt Felhaszáló típusú felhasználó.
     */
    private void userModositasFormCreate(User modifyingUser) {
        // FormsandLists
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        UserDto userDto = formsAndLists.userUpdateForm(modifyingUser);
        // Gomb kreálás
        Button mentesButton = new Button("Mentés");
        mentesButton.getStyleClass().add("defaultButton");

        HBox gombSor = new HBox(mentesButton);
        gombSor.setAlignment(Pos.TOP_CENTER);
        gombSor.setPadding(new Insets(0, 120, 0, 320));
        userDto.getVbox().getChildren().add(gombSor);
        adatokBox.getChildren().add(userDto.getVbox());

        mentesButton.setOnAction((event) -> {
            String firstName = userDto.getFirst_name().getText();
            if (firstName.length() < 2 || firstName.length() > 50) {
                showAlert(Alert.AlertType.ERROR, null, "Validációs hiba!", "A keresztnévnek 2 és 50 karakter között kell lennie!");
                return;
            }

            String lastName = userDto.getLast_name().getText();
            if (lastName.length() < 2 || lastName.length() > 50) {
                showAlert(Alert.AlertType.ERROR, null, "Validációs hiba!", "A vezetéknévnek 2 és 50 karakter között kell lennie!");
                return;
            }

            String email = userDto.getEmail().getText();
            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, null, "Validációs hiba!", "Az email cím nem megfelelő formátumú!");
                return;
            }

            String password = userDto.getPassword().getText();
            if (password == null || password.isEmpty()) {
                password = null;
            } else if (password.length() < 6 || password.length() > 255) {
                showAlert(Alert.AlertType.ERROR, null, "Validációs hiba!", "A jelszónak 6 és 255 karakter között kell lennie! Vagy ha nem adsz meg semmit akkor nem változik.");
                return;
            }

            //dtobol a pizza adatok
            long updateId = modifyingUser.getId();
            // jelszó megadás ellenőrzése
            if (password == null) {
                User noChangePassWordUser = new User(updateId, firstName, lastName, email, userDto.getAdmin().isSelected() ? "ADMIN" : "USER");
                userModositasFelmasolas(noChangePassWordUser, updateId);
            } else {
                User changePasswordUser = new User(updateId, firstName, lastName, email, password, userDto.getAdmin().isSelected() ? "ADMIN" : "USER");
                userModositasFelmasolas(changePasswordUser, updateId);
            }
        });
    }

    /**
     * Beírt email ellenőrzése.
     * @param email String megadott email.
     * @return Boolean, hogy megfelelő-e az email formája.
     */
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Ez a funkció tölti fel a már módosított felhasználó adatait azt adatbázisba.
     * @param readyUser Módosítandó felhasználó adatai.
     * @param updateId Módosítandó felhasználó id-je
     */
    private void userModositasFelmasolas(User readyUser, Long updateId) {
        // Modositas
        HttpResponse<String> response = userRequests.updateUserRequest(readyUser, updateId, USER_URL);
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
    /**
     * Ez a funkció hozza létre a különböző felugró (figyelmeztető/hiba) ablakokat.
     * @param alertType Az alert typusát kell megadni.
     * @param owner Az alert elhelyezéséért felelős Node.
     * @param title Az alert címe.
     * @param message Az alert üzenete.
     */
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    /**
     * Megnézi, hogy melyik tábla kijelölve, amenyiben nincs kijelölve semmi hibát dob, amennyiben pizza van kijelölve hibát dob.
     * Amennyiben felhasználó volt kijelölve meghívja a megerősítoAlert-et.
     */
    public void torlesClick() {
        if (userTable) {
            // Kijelölés ellenőrzése
            User selected = userLista.getSelectionModel().getSelectedItem();
            long selectedIndex = selected.getId();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy felhasználot!");
            } else {
                megerositoAlert(owner, selectedIndex);
            }
        } else {
            Window owner = kilepesButton.getScene().getWindow();
            showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Nem lehet pizzát törölni, csak módosítani!");
        }
    }

    /**
     * Ezen az ablakon döntheti el, hogy az adott felhasználót tényleges szeretné-e törölni.
     * @param owner Az alert elhelyezéséért felelős Node.
     * @param selectedIndex A kijelölt felhasználó indexe a atáblázatból.
     */
    private void megerositoAlert(Window owner, long selectedIndex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Biztos!");
        alert.setHeaderText(null);
        alert.setContentText("Biztosan törölni akarja felhasználot?");
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
        }
    }

    /**
     * Ez a funkció küldi el a kérést, hogy az adott felhasználót a backend törölje az adatbázisból.
     * @param selectedIndex A kijelölt felhasználó indexe a atáblázatból.
     */
    private void userVeglegesTorles(long selectedIndex) {
        // Törlés
        HttpResponse<String> response = userRequests.deleteUserRequest(USER_URL, selectedIndex);
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

    /**
     * A megjelenítő VBox tartalmát eltávolítjuk.
     */
    private void adatokBoxClear() {
        adatokBox.getChildren().clear();
    }
    /**
     * Ez a funkció hívja meg a pizzaTáblázat létrehozását.
     */
    public void pizzaListing() {
        pizzaListCreate();
    }
    /**
     * Ez a funkció hívja meg a pizzaForm létrehozását, amiben új pizzát tud készíteni.
     */
    public void pizzaCreate() {
        addPizzaFormCreate();
    }
    /**
     * Ez a funkció hívja meg a pizza módosítás form létrehozását.
     */
    private void addPizzaFormCreate() {
        adatokBoxClear();
        //Létrehozott formandlist
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createPizza(kilepesButton, pizzaLista));
    }
    /**
     * Ez a funkció hívja meg a felhasználóTáblázat létrehozását.
     */
    private void userListCreate() {
        userTable = true;
        adatokBoxClear();
        userLista.getItems().clear();
        userLista.getColumns().clear();
        //Létrehozott formandlist
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createUserList(userLista));
    }
    /**
     * Ez a funkció hívja meg a pizzaTáblázat létrehozását.
     */
    private void pizzaListCreate() {
        userTable = false;
        adatokBoxClear();
        pizzaLista.getItems().clear();
        pizzaLista.getColumns().clear();
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBox.getChildren().add(formsAndLists.createPizzaList(pizzaLista));
    }
    /**
     * Ez a funkció hívja meg a felhasználóTáblázat létrehozását.
     */
    public void userListing() {
        userListCreate();
    }
    /**
     * Rendelések kilistázása.
     */
    public void rendelesClick() {
        rendelesFormFrissites();
    }

    /**
     * Rendelések frissítése.
     */
    public void rendelesFormFrissites() {
        FormsAndLists formsAndLists = new FormsAndLists(adatokBox);
        adatokBoxClear();
        adatokBox.setAlignment(Pos.TOP_CENTER);
        adatokBox.getChildren().add(formsAndLists.orderListCreate("http://localhost:8080/order"));
    }
}