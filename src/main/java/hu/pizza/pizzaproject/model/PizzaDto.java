package hu.pizza.pizzaproject.model;

import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PizzaDto {
    private VBox vbox;
    private int id;
    private TextField name;
    private TextField picture;
    private TextField description;
    private Spinner<Integer> price;

    public VBox getVbox() {
        return vbox;
    }

    public void setVbox(VBox vbox) {
        this.vbox = vbox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TextField getName() {
        return name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public TextField getPicture() {
        return picture;
    }

    public void setPicture(TextField picture) {
        this.picture = picture;
    }

    public TextField getDescription() {
        return description;
    }

    public void setDescription(TextField description) {
        this.description = description;
    }

    public Spinner<Integer> getPrice() {
        return price;
    }

    public void setPrice(Spinner<Integer> price) {
        this.price = price;
    }
}
