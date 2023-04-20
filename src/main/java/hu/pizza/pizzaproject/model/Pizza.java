package hu.pizza.pizzaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pizza {
    private Long id;
    private String name;
    private int price;
    private String description;
    private String picture;
    private boolean available;

    public Pizza(String name, String picture, String description, int price, boolean available) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.available = available;
    }
}
