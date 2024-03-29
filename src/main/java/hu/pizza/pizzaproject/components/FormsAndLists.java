package hu.pizza.pizzaproject.components;

import hu.pizza.pizzaproject.model.*;
import hu.pizza.pizzaproject.requests.ImgurRequests;
import hu.pizza.pizzaproject.requests.OrderRequests;
import hu.pizza.pizzaproject.requests.PizzaRequests;
import hu.pizza.pizzaproject.requests.UserRequests;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.*;

import static hu.pizza.pizzaproject.components.HomepageController.showAlert;

/**
 * FormsAndLists osztály.
 */
public class FormsAndLists {
    /**
     * VBox ebbe rakjuk bele az itt létre hozott formokat, listákat, egyéb javafx elemeket.
     */
    private final VBox adatokBox;

    /**
     * FormsandLists konstruktora.
     * @param adatokBox VBox-ot vár amibe belerakja az itt elkészült elemeket.
     */
    public FormsAndLists(VBox adatokBox) {
        this.adatokBox = adatokBox;
    }

    /**
     * Rendelések listája.
     */
    private static final List<Order> orders = new ArrayList<>();
    /**
     * Felhasználó kérések osztálya példányosítva.
     */
    private final UserRequests userRequests = new UserRequests();
    /**
     * Pizza kérések osztálya példányosítva.
     */
    private final PizzaRequests pizzaRequests = new PizzaRequests();
    /**
     * Rendelések kérések osztálya példányosítva.
     */
    private final OrderRequests orderRequests = new OrderRequests();
    /**
     * Pizza kérésekhez tartozó alap link.
     */
    private final String PIZZA_URL = "http://localhost:8080/pizza";

    /**
     * Létrehozza az order táblázatot, abból a listából, amit a backendtől kapunk vissza.
     * Amennyiben nincs készülő pizza egy text-et dob vissza "nincs készülő pizza" felírattal
     * @param BASE_URL Egy String "http://localhost:8080/order" ami a backend endpointja.
     * @return Egy VBox-ot add vissza, amit meg jelenít a kezelőfelület ablakon.
     */
    public VBox orderListCreate(String BASE_URL) {
        orders.clear();
        orders.addAll(orderRequests.getallOrderRequest(BASE_URL));
        VBox hboxLista = new VBox();
        hboxLista.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        Label cim = new Label("Rendelések");
        cim.getStyleClass().add("orderLabel");
        cim.setStyle("-fx-text-fill: black;");
        cim.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        HBox cimSor = new HBox(cim);
        HBox.setMargin(cim, new Insets(10, 0, 10, 0));
        cimSor.setAlignment(Pos.TOP_CENTER);

        //Üres lista ellenőrzés
        if (orders.size() < 1) {
            Label szoveg = new Label("Nincs készülő pizza!");
            szoveg.setStyle("-fx-text-fill: black;");
            szoveg.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            VBox semmiCim = new VBox(cim, szoveg);
            semmiCim.setAlignment(Pos.TOP_CENTER);
            return new VBox(semmiCim);
        }else{
            //Create table
            TableView orderTable = new TableView();

            //Userid
            TableColumn<Order, Long> column1 = new TableColumn<>("Felhasználó Id");
            column1.setCellValueFactory(new PropertyValueFactory<>("userId"));

            //Pizza id
            TableColumn<Order, String> column2 = new TableColumn<>("Pizza Id-k");
            column2.setCellValueFactory(new PropertyValueFactory<>("pizzaIdk"));

            //Idopont
            TableColumn<Order, Date> column3 = new TableColumn<>("Dátum");
            column3.setCellValueFactory(new PropertyValueFactory<>("order_date"));

            //Telefon
            TableColumn<Order, String> column4 = new TableColumn<>("Telefonszám");
            column4.setCellValueFactory(new PropertyValueFactory<>("phone_number"));

            //Cím
            TableColumn<Order, String> column5 = new TableColumn<>("Hely");
            column5.setCellValueFactory(new PropertyValueFactory<>("location"));
            column5.setMaxWidth(189);
            column5.setMinWidth(189);

            //Osszeg
            TableColumn<Order, Integer> column6 = new TableColumn<>("Ár");
            column6.setCellValueFactory(new PropertyValueFactory<>("price"));

            //Elkészült gomb
            TableColumn<Order, Void> column7 = new TableColumn<>("Kész");
            Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                    final TableCell<Order, Void> cell = new TableCell<>() {
                        private final Button btn = new Button("Elkészült");
                        {
                            btn.getStyleClass().add("defaultDoneButton");
                            btn.setOnAction((ActionEvent event) -> {
                                long index = getTableView().getItems().get(getIndex()).getId();
                                System.out.println(index);
                                String ORDER_URL = "http://localhost:8080/order";
                                HttpResponse<String> response = orderRequests.updateReadyStatus(index, ORDER_URL);
                                if (response.statusCode() == 200) {
                                    adatokBox.getChildren().clear();
                                    adatokBox.getChildren().add(orderListCreate(ORDER_URL));
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                Order order = getTableView().getItems().get(getIndex());
                                btn.setId(String.valueOf(order.getId())); // set the id of the button to the order id
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }
            };
            column7.setCellFactory(cellFactory);


            orderTable.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
            orderTable.setItems(FXCollections.observableList(orders));
            //Stílus hozzáadása
            orderTable.getStyleClass().add("orderTable");

            //hboxlistába berakni a tableview-ot
            hboxLista.getChildren().add(orderTable);

            // return table in vbox
            VBox kesz = new VBox();
            kesz.getChildren().addAll(cimSor, hboxLista);
            return kesz;
        }
    }

    /**
     * Pizza létrehozáshoz használt funkció, ami egy formot hoz létre egy VBoxon belülre.
     * @param kilepesButton Stage-en megtalálható elem, a pontos elhelyezés érdekében.
     * @param pizzaLista Pizza típusú pizzák listája.
     * @return Vbox-ot ad vissza, amiben megtalálható a pizza létregozási form.
     */
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

        Button feltoltesButton = new Button("Kép kiválasztása");
        feltoltesButton.getStyleClass().add("defaultButton");
        buttonInit(kilepesButton, ex1, feltoltesButton);
        HBox kepSor = new HBox(10, kep, feltoltesButton);

        // ar
        Label ar = new Label("Ár:");
        TextField arField = new TextField();
        arField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                arField.setText(newValue.replaceAll("\\D", ""));
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
                try {
                    Pizza newPizza = new Pizza(nevTextField.getText(), imgurRequests.postImageToImgur(), Integer.parseInt(arField.getText()), true, leirasTextField.getText());
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
                } catch (RuntimeException e) {
                    System.out.println("Kép mérete nagyobb a megengedettnél!");
                }
            }
        });
        return kisablakVbox;
    }

    /**
     *
     * @param kilepesButton Stage-en megtalálható elem, a pontos elhelyezés érdekében.
     * @param ex1 Képfájlok filtere csakis kép fájlokat lehet látni.
     * @param feltoltesButton Gomb aminek a lenyomásával képet tudunk kiválasztani a gépről feltöltésre.
     */
    private void buttonInit(Button kilepesButton, FileChooser.ExtensionFilter ex1, Button feltoltesButton) {
        feltoltesButton.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(ex1);
            Window window = kilepesButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                FilePathAsString.setFilePath(selectedFile.getPath());
            }
        });
    }

    /**
     * Ez a funkció hozza létre a Pizza, illetve a Felhasználó táblázatokat.
     * @param table TableView, ami akármilyen adatokal dolgozhat.
     * @param title String ez lesz a tábla címe/neve.
     * @param columns TableColumn Lista, ami elfogad az oszlopnak Pizza és Felhasználó típusú adatokat is.
     * @param items Lista ami akármilyen adatokat tartalmazhat.
     * @return TableView-t ad vissza a megfelelő típusokkal és elemekkel.
     * @param <T> Lehet Pizza/Felhasználó típusú adat.
     */
    private <T> TableView<T> createTable(TableView<T> table, String title, List<TableColumn<T, ?>> columns, List<T> items) {
        // Tábla cím
        Text text = new Text();
        text.setText(title);
        VBox.setMargin(text, new Insets(10, 0, 10, 0));
        text.setStyle("-fx-fill: black; ");
        text.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        adatokBox.getChildren().add(text);
        adatokBox.setAlignment(Pos.TOP_CENTER);

        table.getColumns().setAll(columns);
        table.setItems(FXCollections.observableArrayList(items));

        return table;
    }

    /**
     * Ez a funkció kéri le az összes pizza adatát majd küldi tovább a createTable funkciónak, hogy az létre hozza a táblázatot.
     * @param pizzaLista Pizza típusú adatokat tartalmazó TableView.
     * @return Vissza adja a createTabletől visszakapott TableView-t és ezt továbbítja;
     */
    public TableView<Pizza> createPizzaList(TableView<Pizza> pizzaLista) {
        List<TableColumn<Pizza, ?>> columns = new ArrayList<>();

        // id
        TableColumn<Pizza, Integer> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columns.add(column1);

        // name
        TableColumn<Pizza, String> column2 = new TableColumn<>("Név");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.add(column2);

        // picture
        TableColumn<Pizza, String> column3 = new TableColumn<>("kép");
        column3.setCellValueFactory(new PropertyValueFactory<>("picture"));
        columns.add(column3);

        // price
        TableColumn<Pizza, Integer> column4 = new TableColumn<>("Ár");
        column4.setCellValueFactory(new PropertyValueFactory<>("price"));
        columns.add(column4);

        // available
        TableColumn<Pizza, Boolean> column5 = new TableColumn<>("Elérhető");
        column5.setCellValueFactory(new PropertyValueFactory<>("available"));
        columns.add(column5);

        // description
        TableColumn<Pizza, String> column6 = new TableColumn<>("Leírás");
        column6.setCellValueFactory(new PropertyValueFactory<>("description"));
        columns.add(column6);

        List<Pizza> pizzaListaKesz = pizzaRequests.getAllPizzaRequest(PIZZA_URL);
        return createTable(pizzaLista, "Pizza adatok", columns, pizzaListaKesz);
    }

    /**
     * Ez a funkció kéri le az összes felhasználó adatát majd küldi tovább a createTable funkciónak, hogy az létre hozza a táblázatot.
     * @param userLista Felhasználó típusú adatokat tartalmazó TableView.
     * @return Vissza adja a createTabletől visszakapott TableView-t és ezt továbbítja;
     */
    public TableView<User> createUserList(TableView<User> userLista) {
        List<TableColumn<User, ?>> columns = new ArrayList<>();

        // id
        TableColumn<User, Integer> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columns.add(column1);

        // email
        TableColumn<User, String> column2 = new TableColumn<>("Email");
        column2.setCellValueFactory(new PropertyValueFactory<>("Email"));
        column2.setMinWidth(317);
        columns.add(column2);

        // lastname
        TableColumn<User, String> column3 = new TableColumn<>("Vezetéknév");
        column3.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        column3.setMinWidth(200);
        columns.add(column3);

        // firstname
        TableColumn<User, String> column4 = new TableColumn<>("Keresztnév");
        column4.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        column4.setMinWidth(200);
        columns.add(column4);

        // admin
        TableColumn<User, Boolean> column5 = new TableColumn<>("Szerep");
        column5.setCellValueFactory(new PropertyValueFactory<>("admin"));
        columns.add(column5);


        String USER_URL = "http://localhost:8080/user";
        List<User> userListaKesz = userRequests.getallUserRequest(USER_URL);
        return createTable(userLista, "Felhasználói adatok", columns, userListaKesz);
    }

    /**
     * Ez a funkció hozza létre a módosítandó pizza formot.
     * @param modifyingPizza Ez a módosítandó pizza adatait tartalmazó pizza típusú változó.
     * @return PizzaDto típusú adatot ad vissza.
     */
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
        FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");

        Button feltoltesButton = new Button("Új kép kiválasztása");
        buttonInit(feltoltesButton, ex1, feltoltesButton);
        feltoltesButton.getStyleClass().add("defaultButton");
        HBox kepSor = new HBox(10, kep, feltoltesButton);

        String price = String.valueOf(modifyingPizza.getPrice());
        // ar
        Label ar = new Label("Ár:");
        TextField arField = new TextField(price);
        arField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                arField.setText(newValue.replaceAll("\\D", ""));
            }
        });
        HBox arSor = new HBox(10, ar, arField);

        Label elerheto = new Label("Elérhető:");
        CheckBox elerhetoCheckBox = new CheckBox();
        elerhetoCheckBox.setSelected(modifyingPizza.isAvailable());
        HBox elerhetoSor = new HBox(10, elerheto, elerhetoCheckBox);

        // kialakítás design:
        adatokBox.setAlignment(Pos.CENTER);
        kisablakVbox.setAlignment(Pos.TOP_CENTER);
        kisablakVbox.setPadding(new Insets(0, 250, 10, 0));
        kisablakVbox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; ");

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
        linkKep.setPadding(new Insets(5, 0, 0, 0));
        ar.setPadding(new Insets(5, 0, 0, 0));
        elerheto.setPadding(new Insets(5, 0, 0, 0));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 35, 0, 0));
        linkKepSor.setPadding(new Insets(10, 0, 0, 0));
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

    /**
     * Ez a funkció hozza létre a módosítandó felhasználó formot.
     * @param modifyingUser Ez a módosítandó felhasználó adatait tartalmazó felhasználó típusú változó.
     * @return UserDto típusú adatot ad vissza.
     */
    public UserDto userUpdateForm(User modifyingUser) {
        System.out.println(modifyingUser.toString());
        // Sceneben form létrehozása (A keszButton kell, mert csak így lehet margint állítani)
        VBox kisablakVbox = new VBox(10);

        // Firtsname
        Label firstName = new Label();
        firstName.setText("Keresztnév:");
        TextField firstNameTextField = new TextField();
        HBox firstNameSor = new HBox(10, firstName, firstNameTextField);

        // Lastsname
        Label lastName = new Label();
        lastName.setText("Vezetéknév:");
        TextField lastNameTextField = new TextField();
        HBox lastNameSor = new HBox(10, lastName, lastNameTextField);

        // Email
        Label email = new Label();
        email.setText("Email:");
        TextField emailTextField = new TextField();
        HBox emailSor = new HBox(10, email, emailTextField);

        // Password
        Label password = new Label();
        password.setText("Jelszó:");
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

        kisablakVbox.setPadding(new Insets(0, 250, 10, 0));
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
        kisablakVbox.getChildren().addAll(lastNameSor, firstNameSor, emailSor, passwordSor, adminSor);
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
