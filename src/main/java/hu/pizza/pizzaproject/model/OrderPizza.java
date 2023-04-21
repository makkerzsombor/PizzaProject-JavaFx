package hu.pizza.pizzaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * RendelésPizza osztály.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderPizza {
    /**
     * Rendelés id-t tartalmazó változó.
     */
    private Long id;
    /**
     * Pizza tárolására Pizza típusú változó.
     */
    private Pizza pizza;
}
