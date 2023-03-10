package hu.pizza.pizzaproject;

import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.Model.JwtToken;
import hu.pizza.pizzaproject.dataClasses.Pizza;
import hu.pizza.pizzaproject.dataClasses.User;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.http.HttpResponse;
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
    private TableView<User> userLista = new TableView<>();
    @FXML
    private TableView<Pizza> pizzaLista = new TableView<>();
    private int Update_id;
    private String USER_URL = "http://localhost:8080/user";
    private String PIZZA_URL = "http://localhost:8080/pizza";
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
    public void keresoClick(ActionEvent actionEvent) {
        //TODO: megkeresni a keresoField elemét a felhasználok táblázatból és az adatBox-ba rakja
    }
    public void modositasClick(ActionEvent actionEvent) {
        if (userTable) {
            int selectedIndex = userLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            }else{
                // adatok mentése
                User selected = userLista.getSelectionModel().getSelectedItem();
                User modifyingUser = new User(selected.getId(), selected.getFirst_name(), selected.getLast_name(), selected.getEmail(), selected.getPassword(), selected.isAdmin());
                userModositasFormCreate(modifyingUser);
            }
        }else {
            int selectedIndex = pizzaLista.getSelectionModel().getSelectedIndex();
            Window owner = kilepesButton.getScene().getWindow();
            if (selectedIndex == -1) {
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Először jelöljön ki egy elemet!");
            }else{
                Pizza selected = pizzaLista.getSelectionModel().getSelectedItem();
                Pizza modifyingPizza = new Pizza(selected.getId(), selected.getName(), selected.getDescription(), selected.getPicture(), selected.getPrice());
                pizzaModositasFormCreate(modifyingPizza);
            }
        }
    }
    private void pizzaModositasFormCreate(Pizza modifyingPizza){
        // Sceneben form létrehozása (A keszButton kell, mert csak így lehet margint állítani)
        VBox kisablakVbox = new VBox(10);

        // Név
        Label nev = new Label();
        nev.setText("Név:");
        TextField nevTextField = new TextField();
        nevTextField.setText(modifyingPizza.getName());
        HBox nevSor = new HBox(10, nev, nevTextField);

        // Leírás
        Label leiras = new Label();
        leiras.setText("Leírás:");
        TextField leirasTextField = new TextField();
        leirasTextField.setText(modifyingPizza.getDescription());
        HBox leirasSor = new HBox(10, leiras, leirasTextField);

        // Kép
        Label kep = new Label();
        kep.setText("Kép:");
        TextField kepTextField = new TextField();
        kepTextField.setText(modifyingPizza.getPicture());
        HBox kepSor = new HBox(10, kep, kepTextField);

        // Ár
        Label Ár = new Label();
        Ár.setText("Ár:");
        Spinner<Integer> arField = new Spinner<>();
        arField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, modifyingPizza.getPrice(), 10));
        HBox arSor = new HBox(10, Ár, arField);

        // Létrehozás
        Button keszButton = new Button();
        keszButton.setText("Mentés");
        HBox buttonSor = new HBox(keszButton);
        HBox.setMargin(keszButton, new Insets(0, 120, 10, 0));
        // sceneClearing
        adatokBoxClear();

        // kialakítás design:
        adatokBox.setAlignment(Pos.CENTER);
        kisablakVbox.setAlignment(Pos.TOP_CENTER);

        kisablakVbox.setPadding(new Insets(0, 320, 10, 0));
        kisablakVbox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        // Hboxok
        nevSor.setAlignment(Pos.TOP_RIGHT);
        leirasSor.setAlignment(Pos.TOP_RIGHT);
        kepSor.setAlignment(Pos.TOP_RIGHT);
        arSor.setAlignment(Pos.TOP_RIGHT);
        buttonSor.setAlignment(Pos.TOP_RIGHT);

        // labelek
        nev.setPadding(new Insets(5, 0, 0, 0));
        leiras.setPadding(new Insets(5, 0, 0, 0));
        kep.setPadding(new Insets(5, 0, 0, 0));
        Ár.setPadding(new Insets(5, 0, 0, 0));
        keszButton.setPadding(new Insets(0, 20, 0, 20));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 0, 0, 0));
        arSor.setPadding(new Insets(10, 0, 0, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(nevSor, leirasSor, kepSor, arSor, buttonSor);
        adatokBox.getChildren().add(kisablakVbox);

        keszButton.setOnAction((event) -> {
            int updateId = modifyingPizza.getId();
            Pizza readyPizza = new Pizza(updateId, nevTextField.getText(), kepTextField.getText(), leirasTextField.getText(), arField.getValue());
            pizzaModositasFelmasolas(readyPizza, updateId);
        });
    }
    private void pizzaModositasFelmasolas(Pizza readyPizza, int updateId){
        // Modositas
        HttpResponse response = requestHandler.updatePizzaRequest(readyPizza, updateId, PIZZA_URL);
        if (response.statusCode() == 200) {
            System.out.println("Siker");
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
        HBox.setMargin(adminCheckbox, new Insets(0, 133, 0, 0));

        // Button
        Button keszButton = new Button();
        HBox modositasSor = new HBox(keszButton);
        HBox.setMargin(keszButton, new Insets(0, 120, 10, 0));

        // Inputokba value-k:
        firstNameTextField.setText(modifyingUser.getFirst_name());
        lastNameTextField.setText(modifyingUser.getLast_name());
        emailTextField.setText(modifyingUser.getEmail());
        passwordTextField.setText(modifyingUser.getPassword());
        keszButton.setText("Mentés");
        if (modifyingUser.isAdmin()) {
            adminCheckbox.setSelected(true);
        } else {
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
        keszButton.setPadding(new Insets(0, 20, 0, 20));

        // Sorok:
        firstNameSor.setPadding(new Insets(10, 0, 0, 0));
        lastNameSor.setPadding(new Insets(10, 0, 0, 0));
        emailSor.setPadding(new Insets(10, 0, 0, 0));
        passwordSor.setPadding(new Insets(10, 0, 0, 0));
        adminSor.setPadding(new Insets(10, 0, 10, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(firstNameSor, lastNameSor, emailSor, passwordSor, adminSor, modositasSor);
        adatokBox.getChildren().add(kisablakVbox);

        keszButton.setOnAction((event) -> {
            System.out.println("Jelszó ellenörzés");
            long updateId = modifyingUser.getId();
            User readyUser = new User(updateId, firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getText(), adminCheckbox.isSelected());
            if (passwordTextField.getText() == null) {
                System.out.println("A jelszo nem változik");
                User noPasswordUser = new User(updateId, firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), adminCheckbox.isSelected());
                userModositasFelmasolas(noPasswordUser, updateId);
            } else {
                userModositasFelmasolas(readyUser, updateId);
                System.out.println("A jelszo változik");
            }
        });
    }
    private void userModositasFelmasolas(User readyUser, long updateId) {
        // Modositas
        HttpResponse response = requestHandler.updateUserRequest(readyUser, updateId, USER_URL);
        if (response.statusCode() == 200) {
            System.out.println("Siker");
            Window window = adatokBox.getScene().getWindow();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres módosítás", "Az adatbázist sikeresen frissitettük");
        } else {
            System.out.println(response.body());
            System.out.println("Valami rossz");
        }
        // Táblázat újra generálása
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
                    System.out.println("OK button clicked");
                    userVeglegesTorles(selectedIndex);
                } else if (buttonType == cancelButton) {
                    System.out.println("Cancel button clicked");
                }
            });
        } else {
            // Pizza
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == okButton) {
                    System.out.println("OK button clicked");
                    pizzaVeglegesTorles(selectedIndex);
                } else if (buttonType == cancelButton) {
                    System.out.println("Cancel button clicked");
                }
            });
        }

    }
    private void userVeglegesTorles(long selectedIndex) {
        System.out.println("User deletebe megy");
        // Törlés
        HttpResponse response = requestHandler.deleteRequest(USER_URL, selectedIndex);
        if (response.statusCode() == 200) {
            System.out.println("Siker");
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
        System.out.println("Pizzás delete-be megy");
        // Törlés
        HttpResponse response = requestHandler.deleteRequest(PIZZA_URL, selectedIndex);
        if (response.statusCode() == 200) {
            System.out.println("Siker");
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
        // Sceneben form létrehozása (A keszButton kell, mert csak így lehet margint állítani)
        VBox kisablakVbox = new VBox(10);

        // Név
        Label nev = new Label();
        nev.setText("Név:");
        TextField nevTextField = new TextField();
        HBox nevSor = new HBox(10, nev, nevTextField);

        // Leírás
        Label leiras = new Label();
        leiras.setText("Leírás:");
        TextField leirasTextField = new TextField();
        HBox leirasSor = new HBox(10, leiras, leirasTextField);

        // Kép
        Label kep = new Label();
        kep.setText("Kép:");
        TextField kepTextField = new TextField();
        HBox kepSor = new HBox(10, kep, kepTextField);

        // Ár
        Label Ár = new Label();
        Ár.setText("Ár:");
        Spinner<Integer> arField = new Spinner<>();
        arField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 2000, 10));
        HBox arSor = new HBox(10, Ár, arField);

        // Létrehozás
        Button keszButton = new Button();
        keszButton.setText("Létrehozás");
        HBox buttonSor = new HBox(keszButton);
        HBox.setMargin(keszButton, new Insets(0, 120, 10, 0));
        // sceneClearing
        adatokBoxClear();

        // kialakítás design:
        adatokBox.setAlignment(Pos.CENTER);
        kisablakVbox.setAlignment(Pos.TOP_CENTER);

        kisablakVbox.setPadding(new Insets(0, 320, 10, 0));
        kisablakVbox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        // Hboxok
        nevSor.setAlignment(Pos.TOP_RIGHT);
        leirasSor.setAlignment(Pos.TOP_RIGHT);
        kepSor.setAlignment(Pos.TOP_RIGHT);
        arSor.setAlignment(Pos.TOP_RIGHT);
        buttonSor.setAlignment(Pos.TOP_RIGHT);

        // labelek
        nev.setPadding(new Insets(5, 0, 0, 0));
        leiras.setPadding(new Insets(5, 0, 0, 0));
        kep.setPadding(new Insets(5, 0, 0, 0));
        Ár.setPadding(new Insets(5, 0, 0, 0));
        keszButton.setPadding(new Insets(0, 20, 0, 20));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 0, 0, 0));
        arSor.setPadding(new Insets(10, 0, 0, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(nevSor, leirasSor, kepSor, arSor, buttonSor);
        adatokBox.getChildren().add(kisablakVbox);

        keszButton.setOnAction((event) -> {
            if (nevTextField.getText().equals("") || leirasTextField.getText().equals("") || kepTextField.getText().equals("")) {
                Window owner = kilepesButton.getScene().getWindow();
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Töltsön ki minden mezőt!");
            } else {
                Pizza newPizza = new Pizza(nevTextField.getText(),  kepTextField.getText(), leirasTextField.getText(), arField.getValue());
                HttpResponse response = requestHandler.addPizzaRequest(PIZZA_URL, newPizza);
                if (response.statusCode() == 200) {
                    System.out.println("Pizza sikeresen létrehozva");
                    Window window = adatokBox.getScene().getWindow();
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres létrehozás", "Az adott pizzát sikeresen létrehoztuk");
                } else {
                    System.out.println("Nem jó");
                    Window window = adatokBox.getScene().getWindow();
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Hiba történt", "Dolgozunk rajta!");
                }
                pizzaListCreate();
            }
        });

    }
    private void userListCreate() {
        userTable = true;
        adatokBoxClear();
        userLista.getItems().clear();
        userLista.getColumns().clear();

        // Tábla cím
        Text text = new Text();
        text.setText("User adatok");
        adatokBox.setMargin(text, new Insets(0, 0, 10, 0));
        text.setStyle("-fx-fill: white; ");
        text.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.TOP_CENTER);

        // id
        TableColumn<User, Integer> column1 =
                new TableColumn<>("Id");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Id"));

        // email
        TableColumn<User, String> column2 =
                new TableColumn<>("Email");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("Email"));

        // lastname
        TableColumn<User, String> column3 =
                new TableColumn<>("Lastname");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("last_name"));

        // firstname
        TableColumn<User, String> column4 =
                new TableColumn<>("Firstname");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("first_name"));

        // admin
        TableColumn<User, Boolean> column5 =
                new TableColumn<>("Admin");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("admin"));

        userLista.getColumns().addAll(column1, column2, column3, column4, column5);

        adatokBox.getChildren().add(userLista);

        List<User> userLista = requestHandler.getallUserRequest(USER_URL);
        // Listából tableViewba rakás
        this.userLista.setItems(FXCollections.observableArrayList(userLista));
    }
    private void pizzaListCreate() {
        userTable = false;
        adatokBoxClear();
        pizzaLista.getItems().clear();
        pizzaLista.getColumns().clear();

        // Tábla cím
        Text text = new Text();
        text.setText("Pizza adatok");
        adatokBox.setMargin(text, new Insets(0, 0, 10, 0));
        text.setStyle("-fx-fill: white; ");
        text.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.TOP_CENTER);

        // id
        TableColumn<Pizza, Integer> column1 =
                new TableColumn<>("Id");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("Id"));

        // name
        TableColumn<Pizza, String> column2 =
                new TableColumn<>("Name");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        // description
        TableColumn<Pizza, String> column3 =
                new TableColumn<>("Description");

        column3.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        // picture
        TableColumn<Pizza, String> column4 =
                new TableColumn<>("Picture");

        column4.setCellValueFactory(
                new PropertyValueFactory<>("picture"));

        // price
        TableColumn<Pizza, Integer> column5 =
                new TableColumn<>("Price");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("price"));

        pizzaLista.getColumns().addAll(column1, column2, column3, column4, column5);

        adatokBox.getChildren().add(pizzaLista);
        List<Pizza> pizzaListaKesz = requestHandler.getallPizzaRequest(PIZZA_URL);
        // Listából tableViewba rakás
        this.pizzaLista.setItems(FXCollections.observableArrayList(pizzaListaKesz));
    }
    public void userListing(ActionEvent actionEvent) {
        userListCreate();
    }
}