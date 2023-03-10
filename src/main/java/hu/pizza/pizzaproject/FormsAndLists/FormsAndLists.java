package hu.pizza.pizzaproject.FormsAndLists;

import hu.pizza.pizzaproject.DataClasses.Order;
import hu.pizza.pizzaproject.DataClasses.Pizza;
import hu.pizza.pizzaproject.DataClasses.User;
import hu.pizza.pizzaproject.Dtos.PizzaDto;
import hu.pizza.pizzaproject.RequestHandler;
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
import javafx.stage.Window;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static hu.pizza.pizzaproject.HomepageController.showAlert;

public class FormsAndLists {
    private VBox adatokBox;

    public FormsAndLists(VBox adatokBox) {
        this.adatokBox = adatokBox;
    }

    private static List<Order> orders = new ArrayList<>();
    private RequestHandler requestHandler = new RequestHandler();
    private String ORDER_URL = "http://localhost:8080/order";
    private String PIZZA_URL = "http://localhost:8080/pizza";
    private String USER_URL = "http://localhost:8080/user";

    public VBox orderListCreate(String BASE_URL) {
        orders.clear();
        orders.addAll(requestHandler.getallOrderRequest(BASE_URL));
        VBox hboxLista = new VBox();
        hboxLista.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");
        String orderElem = "";
        Label cim = new Label("Rendelések");
        cim.setStyle("-fx-text-fill: White;");
        cim.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        HBox cimSor = new HBox(cim);
        cimSor.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < orders.size(); i++) {
            if (!orders.get(i).isReady()) {
                orderElem = "userId: " + String.valueOf(orders.get(i).getUser_id()) + " pizzaId: " + String.valueOf(orders.get(i).getPizza_id()) + " Időpont: " +
                        String.valueOf(orders.get(i).getOrder_date()) + " Telefon: " + String.valueOf(orders.get(i).getPhone_number()) + " Cím: " +
                        String.valueOf(orders.get(i).getLocation());
                Label label = new Label(orderElem);
                Button readyButton = new Button("Elkészült");
                readyButton.setId(String.valueOf(orders.get(i).getId()));
                readyButton.setOnAction((event) -> {
                    handleOrderDone(readyButton.getId());
                });
                HBox elemSor = new HBox(label, readyButton);
                elemSor.setSpacing(10);
                elemSor.setAlignment(Pos.TOP_RIGHT);
                elemSor.setPadding(new Insets(5, 120, 5, 0));
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
    public void handleOrderDone(String elem) {
        long index = Integer.parseInt(elem.substring(0, 1));
        HttpResponse response = requestHandler.updateReadyStatus(index, ORDER_URL);
        if (response.statusCode() == 200) {
            adatokBox.getChildren().clear();
            adatokBox.getChildren().add(orderListCreate(ORDER_URL));
        }
    }
    public VBox createPizza(Button kilepesButton, TableView<Pizza> pizzaLista) {
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

        keszButton.setOnAction((event) -> {
            if (nevTextField.getText().equals("") || leirasTextField.getText().equals("") || kepTextField.getText().equals("")) {
                Window owner = kilepesButton.getScene().getWindow();
                showAlert(Alert.AlertType.ERROR, owner, "Használati hiba!", "Töltsön ki minden mezőt!");
            } else {
                Pizza newPizza = new Pizza(nevTextField.getText(), kepTextField.getText(),leirasTextField.getText(), arField.getValue());
                HttpResponse response = requestHandler.addPizzaRequest(PIZZA_URL, newPizza);
                if (response.statusCode() == 200) {
                    System.out.println("Pizza sikeresen létrehozva");
                    Window window = adatokBox.getScene().getWindow();
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

        List<Pizza> pizzaListaKesz = requestHandler.getallPizzaRequest(PIZZA_URL);
        // Listából tableViewba rakás
        pizzaLista.setItems(FXCollections.observableArrayList(pizzaListaKesz));
        return pizzaLista;
    }

    public TableView createUserList(TableView<User> userLista) {
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

        List<User> UserListaKesz = requestHandler.getallUserRequest(USER_URL);
        // Listából tableViewba rakás
        userLista.setItems(FXCollections.observableArrayList(UserListaKesz));
        return userLista;
    }

    public PizzaDto pizzaUpdateForm(Pizza modifyingPizza){
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

        // labelek
        nev.setPadding(new Insets(5, 0, 0, 0));
        leiras.setPadding(new Insets(5, 0, 0, 0));
        kep.setPadding(new Insets(5, 0, 0, 0));
        Ár.setPadding(new Insets(5, 0, 0, 0));

        // Sorok:
        nevSor.setPadding(new Insets(10, 0, 0, 0));
        leirasSor.setPadding(new Insets(10, 0, 0, 0));
        kepSor.setPadding(new Insets(10, 0, 0, 0));
        arSor.setPadding(new Insets(10, 0, 0, 0));

        // Vboxba a hboxok
        kisablakVbox.getChildren().addAll(nevSor, leirasSor, kepSor, arSor);
        System.out.println("Létrehozza a pizza modositas formot");

        // new pizzadto
        PizzaDto pizzaDto = new PizzaDto();
        // feltöltés
        pizzaDto.setVbox(kisablakVbox);
        pizzaDto.setName(nevTextField);
        pizzaDto.setDescription(leirasTextField);
        pizzaDto.setPicture(kepTextField);
        pizzaDto.setPrice(arField);
        //Visszaadás
        return pizzaDto;
    }

}
