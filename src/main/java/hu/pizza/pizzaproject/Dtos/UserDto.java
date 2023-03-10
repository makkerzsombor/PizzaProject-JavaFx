package hu.pizza.pizzaproject.Dtos;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserDto {
    private VBox vbox;
    private Long id;
    private TextField email;
    private TextField password = null;
    private CheckBox admin;
    private TextField first_name;
    private TextField last_name;

    public VBox getVbox() {
        return vbox;
    }

    public void setVbox(VBox vbox) {
        this.vbox = vbox;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public TextField getPassword() {
        return password;
    }

    public void setPassword(TextField password) {
        this.password = password;
    }

    public CheckBox getAdmin() {
        return admin;
    }

    public void setAdmin(CheckBox admin) {
        this.admin = admin;
    }

    public TextField getFirst_name() {
        return first_name;
    }

    public void setFirst_name(TextField first_name) {
        this.first_name = first_name;
    }

    public TextField getLast_name() {
        return last_name;
    }

    public void setLast_name(TextField last_name) {
        this.last_name = last_name;
    }
}
