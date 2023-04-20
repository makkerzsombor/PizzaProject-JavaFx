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
    private VBox vbox;
    private Long id;
    private TextField first_name;
    private TextField last_name;
    private TextField email;
    private TextField password = null;
    private CheckBox admin;
}
