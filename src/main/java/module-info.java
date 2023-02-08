module hu.pizza.pizzaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.pizza.pizzaproject to javafx.fxml;
    exports hu.pizza.pizzaproject;
}