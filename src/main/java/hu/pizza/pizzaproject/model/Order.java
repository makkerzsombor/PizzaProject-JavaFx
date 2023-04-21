package hu.pizza.pizzaproject.model;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private Long id;
    private Long userId;
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

    public String getPizzaIdk(){
        return orderPizzas.stream().map(orderPizza -> orderPizza.getId().toString()).collect(Collectors.joining(", "));
    }
}
