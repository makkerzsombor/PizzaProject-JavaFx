package hu.pizza.pizzaproject.components;

import hu.pizza.pizzaproject.model.*;
import hu.pizza.pizzaproject.requests.ImgurRequests;
import hu.pizza.pizzaproject.requests.OrderRequests;
import hu.pizza.pizzaproject.requests.PizzaRequests;
import hu.pizza.pizzaproject.requests.UserRequests;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hu.pizza.pizzaproject.components.HomepageController.showAlert;

public class FormsAndLists {
    private final VBox adatokBox;

    public FormsAndLists(VBox adatokBox) {
        this.adatokBox = adatokBox;
    }

    private static final List<Order> orders = new ArrayList<>();
    private final UserRequests userRequests = new UserRequests();
    private final PizzaRequests pizzaRequests = new PizzaRequests();
    private final OrderRequests orderRequests = new OrderRequests();
    private final String ORDER_URL = "http://localhost:8080/order";
    private final String PIZZA_URL = "http://localhost:8080/pizza";
    private final String USER_URL = "http://localhost:8080/user";

    public VBox orderListCreate(String BASE_URL) {
        orders.clear();
        orders.addAll(orderRequests.getallOrderRequest(BASE_URL));
        VBox hboxLista = new VBox();
        hboxLista.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        String orderElem = "";
        Label cim = new Label("Rendelések");
        cim.getStyleClass().add("orderLabel");
        cim.setStyle("-fx-text-fill: black;");
        cim.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        HBox cimSor = new HBox(cim);
        HBox.setMargin(cim, new Insets(10, 0, 10, 0));
        cimSor.setAlignment(Pos.TOP_CENTER);

        // Van e egyáltalán nem kész pizza
        int notDoneCounter = 0;
        for (Order value : orders) {
            if (!value.isReady()) {
                notDoneCounter++;
            }
        }
        // Nincs mit megjeleniteni
        if (notDoneCounter == 0) {
            Label szoveg = new Label("Nincs készülő pizza!");
            szoveg.setStyle("-fx-text-fill: black;");
            szoveg.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            VBox semmiCim = new VBox(cim, szoveg);
            semmiCim.setAlignment(Pos.TOP_CENTER);
            return new VBox(semmiCim);
        } else {
            for (Order order : orders) {
                if (!order.isReady()) {
                    List<Long> ids = new ArrayList<>();
                    for (var i = 0; i < order.getOrderPizzas().size(); i++){
                        ids.add(order.getOrderPizzas().get(i).getId());
                    }
                    orderElem = "userId: " + order.getUser_id() + " pizzaIds: " + ids + " Időpont: " +
                            order.getOrder_date() + " Telefon: " + order.getPhone_number() + " Cím: " +
                            order.getLocation() + " Összeg: " + order.getPrice();
                    Label label = new Label(orderElem);
                    Button readyButton = new Button("Elkészült");
                    readyButton.getStyleClass().add("defaultButton");
                    readyButton.setId(String.valueOf(order.getId()));
                    readyButton.setOnAction((event) -> {
                        handleOrderDone(readyButton.getId());
                    });
                    HBox elemSor = new HBox(label, readyButton);
                    elemSor.setSpacing(10);
                    elemSor.setAlignment(Pos.TOP_RIGHT);
                    elemSor.setPadding(new Insets(5, 10, 5, 0));
                    label.setPadding(new Insets(5, 0, 0, 0));
                    hboxLista.getChildren().addAll(elemSor);
                    hboxLista.setAlignment(Pos.TOP_CENTER);
                }
            }
            VBox kesz = new VBox();
            VBox cimPLuszLista = new VBox(cimSor, hboxLista);
            kesz.getChildren().add(cimPLuszLista);
            return kesz;
        }
    }

    public void handleOrderDone(String elem) {
        long index = Integer.parseInt(elem.substring(0, 1));
        HttpResponse<String> response = orderRequests.updateReadyStatus(index, ORDER_URL);
        if (response.statusCode() == 200) {
            adatokBox.getChildren().clear();
            adatokBox.getChildren().add(orderListCreate(ORDER_URL));
        }
    }

    public VBox createPizza(Button kilepesButton, TableView<Pizza> pizzaLista) {
        // Sceneben form létrehozása (A keszButton kell, mert csak így lehet margint állítani)
        FilePathAsString.setFilePath("");
        VBox kisablakVbox = new VBox(10);

        // Név
        Label nev = new Label("Név:");
        TextField nevTextField = new TextField();
        HBox nevSor = new HBox(10, nev, nevTextField);

        // Leírás
        Label leiras = new Label("Leírás:");
        TextField leirasTextField = new TextField();
        HBox leirasSor = new HBox(10, leiras, leirasTextField);

        // Kép
        Label kep = new Label("Kép:");
        // Extension filter
        FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        FileChooser.ExtensionFilter ex2 = new FileChooser.ExtensionFilter("All Files", "*.*");

        Button feltoltesButton = new Button("Kép kiválasztása");
        feltoltesButton.getStyleClass().add("defaultButton");
        feltoltesButton.setOnAction((event) ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(ex1, ex2);
            Window window = kilepesButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                FilePathAsString.setFilePath(selectedFile.getPath());
            }
        });
        HBox kepSor = new HBox(10, kep, feltoltesButton);

        // ar
        Label ar = new Label("ár:");
        TextField arField = new TextField();
        arField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                arField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        HBox arSor = new HBox(10, ar, arField);

        // Létrehozás
        Button keszButton = new Button("Létrehozás");
        keszButton.getStyleClass().add("defaultButton");
        HBox buttonSor = new HBox(keszButton);
        HBox.setMargin(keszButton, new Insets(0, 100, 10, 0));

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
        ar.setPadding(new Insets(5, 0, 0, 0));
        keszButton.setPadding(new Insets(0, 20, 0, 20));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 47, 0, 0));
        arSor.setPadding(new Insets(10, 0, 0, 0));


        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(nevSor, leirasSor, kepSor, arSor, buttonSor);

        keszButton.setOnAction((event) -> {
            if (nevTextField.getText().equals("") || leirasTextField.getText().equals("") || FilePathAsString.getFilePath().equals("")) {
                Window owner = kilepesButton.getScene().getWindow();
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Töltsön ki minden mezőt és töltsön fel egy képet!");
            } else {
                // Helyes generálás
                ImgurRequests imgurRequests = new ImgurRequests();
                Pizza newPizza = new Pizza(nevTextField.getText(), imgurRequests.postImageToImgur(), leirasTextField.getText(), Integer.parseInt(arField.getText()), true);
                HttpResponse<String> response = pizzaRequests.addPizzaRequest(PIZZA_URL, newPizza);
                if (response.statusCode() == 200) {
                    Window window = adatokBox.getScene().getWindow();
                    System.out.println("Ez a filepath: " + FilePathAsString.getFilePath());
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Sikeres létrehozás", "Az adott pizzát sikeresen létrehoztuk");
                    // táblázatból törlés
                    pizzaLista.getItems().clear();
                    // lista firssitése
                    adatokBox.getChildren().clear();
                    adatokBox.getChildren().add(createPizzaList(pizzaLista));
                } else if (response.statusCode() == 409) {
                    Window window = adatokBox.getScene().getWindow();
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Hiba történt", "Ilyen nevű pizza már létezik!");
                } else {
                    System.out.println(response.statusCode());
                    Window window = adatokBox.getScene().getWindow();
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Hiba történt", "Dolgozunk rajta!");
                }
            }
        });
        return kisablakVbox;
    }

    public TableView createPizzaList(TableView<Pizza> pizzaLista) {
        // Tábla cím
        Text text = new Text();
        text.setText("Pizza adatok");
        VBox.setMargin(text, new Insets(10, 0, 10, 0));
        text.setStyle("-fx-fill: black; ");
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

        // available
        TableColumn<Pizza, Boolean> column6 =
                new TableColumn<>("Available");

        column6.setCellValueFactory(
                new PropertyValueFactory<>("available"));

        //elözetes törlések
        pizzaLista.getColumns().clear();
        pizzaLista.getItems().clear();

        pizzaLista.getColumns().addAll(column1, column2, column4, column5, column6, column3);

        List<Pizza> pizzaListaKesz = pizzaRequests.getAllPizzaRequest(PIZZA_URL);
        // Listából tableViewba rakás
        pizzaLista.setItems(FXCollections.observableArrayList(pizzaListaKesz));
        return pizzaLista;
    }

    public TableView createUserList(TableView<User> userLista) {
        // Tábla cím
        Text text = new Text();
        text.setText("User adatok");
        VBox.setMargin(text, new Insets(10, 0, 10, 0));
        text.setStyle("-fx-fill: black; ");
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
                new TableColumn<>("Role");

        column5.setCellValueFactory(
                new PropertyValueFactory<>("admin"));

        userLista.getColumns().addAll(column1, column2, column3, column4, column5);

        List<User> UserListaKesz = userRequests.getallUserRequest(USER_URL);
        // Listából tableViewba rakás
        userLista.setItems(FXCollections.observableArrayList(UserListaKesz));
        return userLista;
    }

    public PizzaDto pizzaUpdateForm(Pizza modifyingPizza) {
        FilePathAsString.setFilePath("");
        VBox kisablakVbox = new VBox(10);

        // Név
        Label nev = new Label("Név:");
        TextField nevTextField = new TextField();
        nevTextField.setText(modifyingPizza.getName());
        HBox nevSor = new HBox(10, nev, nevTextField);

        // Leírás
        Label leiras = new Label("Leírás:");
        TextField leirasTextField = new TextField();
        leirasTextField.setText(modifyingPizza.getDescription());
        HBox leirasSor = new HBox(10, leiras, leirasTextField);

        // már fent lévő pizza(link megváltoztatása)
        Label linkKep = new Label("Már fent lévő link:");
        TextField kepTextField = new TextField();
        kepTextField.setText(modifyingPizza.getPicture());
        HBox linkKepSor = new HBox(10, linkKep, kepTextField);

        // újkép feltöltése:
        Label kep = new Label("Új kép feltöltése:");
        FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg");
        FileChooser.ExtensionFilter ex2 = new FileChooser.ExtensionFilter("All Files","*.*");

        Button feltoltesButton = new Button("Új kép kiválasztása");
        feltoltesButton.setOnAction((event) ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(ex1, ex2);
            Window window = feltoltesButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                FilePathAsString.setFilePath(selectedFile.getPath());
                // FilePathAsString.getFilePath();-el lehet lekérni az adott stringet
            }
        });
        feltoltesButton.getStyleClass().add("defaultButton");
        HBox kepSor = new HBox(10, kep, feltoltesButton);

        String price = String.valueOf(modifyingPizza.getPrice());
        // ar
        Label ar = new Label("ar:");
        TextField arField = new TextField(price);
        arField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                arField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        HBox arSor = new HBox(10, ar, arField);

        Label elerheto = new Label("Elérhető: ");
        CheckBox elerhetoCheckBox = new CheckBox();
        if (modifyingPizza.isAvailable()) {
            elerhetoCheckBox.setSelected(true);
        } else {
            elerhetoCheckBox.setSelected(false);
        }
        HBox elerhetoSor = new HBox(10, elerheto, elerhetoCheckBox);

        // kialakítás design:
        adatokBox.setAlignment(Pos.CENTER);
        kisablakVbox.setAlignment(Pos.TOP_CENTER);

        kisablakVbox.setPadding(new Insets(0, 320, 10, 0));
        kisablakVbox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        // Hboxok
        nevSor.setAlignment(Pos.TOP_RIGHT);
        leirasSor.setAlignment(Pos.TOP_RIGHT);
        linkKepSor.setAlignment(Pos.TOP_RIGHT);
        kepSor.setAlignment(Pos.TOP_RIGHT);
        arSor.setAlignment(Pos.TOP_RIGHT);
        elerhetoSor.setAlignment(Pos.TOP_RIGHT);

        // labelek
        nev.setPadding(new Insets(5, 0, 0, 0));
        leiras.setPadding(new Insets(5, 0, 0, 0));
        kep.setPadding(new Insets(5, 0, 0, 0));
        linkKep.setPadding(new Insets(5,0,0,0));
        ar.setPadding(new Insets(5, 0, 0, 0));
        elerheto.setPadding(new Insets(5, 0, 0, 0));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 35, 0, 0));
        linkKepSor.setPadding(new Insets(10,0,0,0));
        arSor.setPadding(new Insets(10, 0, 0, 0));
        elerhetoSor.setPadding(new Insets(10, 130, 0, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(nevSor, leirasSor, linkKepSor, kepSor, arSor, elerhetoSor);

        // new pizzadto
        PizzaDto pizzaDto = new PizzaDto();
        // feltöltés
        pizzaDto.setVbox(kisablakVbox);
        pizzaDto.setName(nevTextField);
        pizzaDto.setDescription(leirasTextField);
        pizzaDto.setPicture(kepTextField);
        pizzaDto.setPrice(arField);
        pizzaDto.setAvailable(elerhetoCheckBox);
        //Visszaadás
        return pizzaDto;
    }

    public UserDto userUpdateForm(User modifyingUser) {
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

        // Inputokba value-k:
        firstNameTextField.setText(modifyingUser.getFirst_name());
        lastNameTextField.setText(modifyingUser.getLast_name());
        emailTextField.setText(modifyingUser.getEmail());
        passwordTextField.setText(modifyingUser.getPassword());
        adminCheckbox.setSelected(Objects.equals(modifyingUser.getAdmin(), "ADMIN"));

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

        // labelek
        firstName.setPadding(new Insets(5, 0, 0, 0));
        lastName.setPadding(new Insets(5, 0, 0, 0));
        email.setPadding(new Insets(5, 0, 0, 0));
        password.setPadding(new Insets(5, 0, 0, 0));
        admin.setPadding(new Insets(0, 0, 0, 0));

        // Sorok:
        firstNameSor.setPadding(new Insets(10, 0, 0, 0));
        lastNameSor.setPadding(new Insets(10, 0, 0, 0));
        emailSor.setPadding(new Insets(10, 0, 0, 0));
        passwordSor.setPadding(new Insets(10, 0, 0, 0));
        adminSor.setPadding(new Insets(10, 0, 10, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(firstNameSor, lastNameSor, emailSor, passwordSor, adminSor);
        // new userdto
        UserDto userDto = new UserDto();
        // feltöltés
        userDto.setVbox(kisablakVbox);
        userDto.setFirst_name(firstNameTextField);
        userDto.setLast_name(lastNameTextField);
        userDto.setEmail(emailTextField);
        userDto.setPassword(passwordTextField);
        userDto.setAdmin(adminCheckbox);
        return userDto;
    }

}
