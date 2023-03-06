module hu.pizza.pizzaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;


    opens hu.pizza.pizzaproject to javafx.fxml, com.google.gson;
    exports hu.pizza.pizzaproject;
    opens hu.pizza.pizzaproject.Model to com.google.gson;
}