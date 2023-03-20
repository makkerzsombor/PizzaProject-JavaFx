package hu.pizza.pizzaproject.model;

public class Pizza {
    private int id;
    private String name;
    private String picture;
    private String description;
    private int price;
    private boolean available;
    public Pizza(int id, String name, String picture, String description, int price, boolean available) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public Pizza(String name, String picture, String description, int price, boolean available) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public Pizza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
