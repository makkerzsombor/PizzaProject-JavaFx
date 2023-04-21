package hu.pizza.pizzaproject.model;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rendelés osztály
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    /**
     * Rendelés id.
     */
    private Long id;
    /**
     * Rendelés felhasználójának id-je.
     */
    private Long userId;
    /**
     * Rendelés helyszíne (Hova szállítsák ki).
     */
    private String location;
    /**
     * Rendelés leadási időpontja.
     */
    private Date order_date;
    /**
     * Rendelés vég összege.
     */
    private int price;
    /**
     * Rendeléskor megadott telefonszám.
     */
    private String phone_number;
    /**
     * Rendelés státusza amennyiben ready már elkészült a pizza és szállítjuk a helyszínre.
     */
    private boolean ready;
    /**
     *
     */
    private List<OrderPizza> orderPizzas;

    /**
     * Rendelés konstruktorja.
     * @param id Long változót vár ez lesz az rendelés id-je.
     * @param ready Boolean áltozót vár ez lesz az rendelés státusza.
     */
    public Order(Long id, boolean ready) {
        this.id = id;
        this.ready = ready;
    }

    /**
     * Ez a funkció a pizzák id-jeit adja vissza vesszővel + space-el elválasztva.
     * @return String (pl: 1, 2, 4)
     */
    public String getPizzaIdk(){
        return orderPizzas.stream().map(orderPizza -> orderPizza.getId().toString()).collect(Collectors.joining(", "));
    }
}
