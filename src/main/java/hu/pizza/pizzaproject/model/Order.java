package hu.pizza.pizzaproject.model;

import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private Long user_id;
    private String location;
    private Date order_date;
    private int price;
    private String phone_number;
    private boolean ready;
    private List<Order> orderPizzas;

    public Order(Long id, boolean ready) {
        this.id = id;
        this.ready = ready;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public List<Order> getOrderPizzas() {
        return orderPizzas;
    }

    public void setOrderPizzas(List<Order> orderPizzas) {
        this.orderPizzas = orderPizzas;
    }

    public Order(Long id, Long user_id, String location, Date order_date, int price, String phone_number, boolean ready, List<Order> orderPizzas) {
        this.id = id;
        this.user_id = user_id;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
        this.orderPizzas = orderPizzas;
    }
}
