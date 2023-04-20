module hu.pizza.pizzaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires lombok;

    opens hu.pizza.pizzaproject to javafx.fxml, com.google.gson, lombok;
    exports hu.pizza.pizzaproject;
    exports hu.pizza.pizzaproject.requests;
    opens hu.pizza.pizzaproject.requests to com.google.gson, javafx.fxml, lombok;
    opens hu.pizza.pizzaproject.auth to com.google.gson, lombok;
    exports hu.pizza.pizzaproject.model;
    opens hu.pizza.pizzaproject.model to com.google.gson, javafx.fxml, lombok;
    exports hu.pizza.pizzaproject.components;
    opens hu.pizza.pizzaproject.components to com.google.gson, javafx.fxml, lombok;
}
