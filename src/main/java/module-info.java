module hu.pizza.pizzaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens hu.pizza.pizzaproject to javafx.fxml;
    exports hu.pizza.pizzaproject;

}