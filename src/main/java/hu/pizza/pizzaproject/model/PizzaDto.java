package hu.pizza.pizzaproject.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PizzaDto {
    private VBox vbox;
    private int id;
    private TextField name;
    private TextField picture;
    private TextField description;
    private TextField price;
    private CheckBox available;
}
