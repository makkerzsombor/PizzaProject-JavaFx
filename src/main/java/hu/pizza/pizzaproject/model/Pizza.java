package hu.pizza.pizzaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Pizza osztály.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pizza {
    /**
     * Pizza id-je.
     */
    private Long id;
    /**
     * Pizza neve.
     */
    private String name;
    /**
     * Pizza ára.
     */
    private int price;
    /**
     * Pizza leírása.
     */
    private String description;
    /**
     * Pizza képe (linkként).
     */
    private String picture;
    /**
     * Pizza elérhetősége (megadja, hogy lehet-e ilyen pizzát rendelni).
     */
    private boolean available;

    /**
     * Pizza konstruktor
     * @param name Az új Pizza nevét állítja be String.
     * @param picture Az új Pizza képét(link) állítja be String.
     * @param price Az új Pizza árát állítja be Integer.
     * @param available Az új Pizza elérhetőségét állítja be boolean.
     * @param description Az új Pizza leírását állítja be String.
     */
    public Pizza(String name, String picture, int price, boolean available, String description) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.available = available;
    }
}
