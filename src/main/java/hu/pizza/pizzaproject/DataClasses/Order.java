package hu.pizza.pizzaproject.DataClasses;

import java.util.Date;

public class Order {
    private long id;
    private long user_id;
    private long pizza_id;
    private String location;
    private Date order_date;
    private String phone_number;

    private int price;
    private boolean ready;

    public Order() {
    }

    public Order(long id, boolean ready) {
        this.id = id;
        this.ready = ready;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getPizza_id() {
        return pizza_id;
    }

    public void setPizza_id(long pizza_id) {
        this.pizza_id = pizza_id;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
