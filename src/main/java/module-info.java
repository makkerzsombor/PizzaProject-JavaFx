module hu.pizza.pizzaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;


    opens hu.pizza.pizzaproject to javafx.fxml, com.google.gson;
    exports hu.pizza.pizzaproject;
    opens hu.pizza.pizzaproject.Model to com.google.gson;
    exports hu.pizza.pizzaproject.DataClasses;
    opens hu.pizza.pizzaproject.DataClasses to com.google.gson, javafx.fxml;
    exports hu.pizza.pizzaproject.Dtos;
    opens hu.pizza.pizzaproject.Dtos to com.google.gson, javafx.fxml;
}