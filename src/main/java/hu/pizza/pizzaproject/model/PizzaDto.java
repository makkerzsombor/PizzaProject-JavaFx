package hu.pizza.pizzaproject.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * PizzaDto osztály.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PizzaDto {
    /**
     * Pizza módosításhoz formot tartalmazó VBox.
     */
    private VBox vbox;
    /**
     * Pizza id-jét tárolja.
     */
    private int id;
    /**
     * Pizza nevét tároló TextField.
     */
    private TextField name;
    /**
     * Pizza képét tároló TextField.
     */
    private TextField picture;
    /**
     * Pizza leírását tároló TextField.
     */
    private TextField description;
    /**
     * Pizza árát tároló TextField.
     */
    private TextField price;
    /**
     * Pizza elérhetőségét(boolean) tároló CheckBox.
     */
    private CheckBox available;
}
