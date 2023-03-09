package hu.pizza.pizzaproject.FormsAndLists;

import hu.pizza.pizzaproject.DataClasses.Order;
import hu.pizza.pizzaproject.RequestHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FormsAndLists {
    private VBox adatokBox;

    public FormsAndLists(VBox adatokBox) {
        this.adatokBox = adatokBox;
    }

    private static List<Order> orders = new ArrayList<>();
    private RequestHandler requestHandler = new RequestHandler();
    private String ORDER_URL = "http://localhost:8080/order";
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
                elemSor.setPadding(new Insets(5,120,5,0));
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
        long index = Integer.parseInt(elem.substring(0,1));
        HttpResponse response = requestHandler.updateReadyStatus(index, ORDER_URL);
        if (response.statusCode() == 200) {
            adatokBox.getChildren().clear();
            adatokBox.getChildren().add(orderListCreate(ORDER_URL));
        }
    }
}
