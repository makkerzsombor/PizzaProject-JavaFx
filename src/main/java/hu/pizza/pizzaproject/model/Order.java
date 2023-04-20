package hu.pizza.pizzaproject.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private Long id;
    private Long user_id;
    private String location;
    private Date order_date;
    private int price;
    private String phone_number;
    private boolean ready;
    private List<OrderPizza> orderPizzas;

    public Order(Long id, boolean ready) {
        this.id = id;
        this.ready = ready;
    }
}
