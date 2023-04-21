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

public class UserDto {
    /**
     * Felhasználó módosításhoz formot tartalmazó VBox.
     */
    private VBox vbox;
    /**
     * Felhasználó id-jét tárolja.
     */
    private Long id;
    /**
     * Felhasználó keresztnevét tároló TextField.
     */
    private TextField first_name;
    /**
     * Felhasználó vezetéknevét tároló TextField.
     */
    private TextField last_name;
    /**
     * Felhasználó emailjét tároló TextField.
     */
    private TextField email;
    /**
     * Felhasználó jelszavát tároló TextField.
     */
    private TextField password = null;
    /**
     * Felhasználó jogait(boolean) tároló CheckBox.
     */
    private CheckBox admin;
}
