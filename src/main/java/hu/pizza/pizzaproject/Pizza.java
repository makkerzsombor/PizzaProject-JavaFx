package hu.pizza.pizzaproject;

public class Pizza {
    private int id;
    private String pizza_name;
    private String picture;
    private String description;
    private float rating;
    private int price;

    public Pizza(int id, String pizza_name, String picture, String description, float rating, int price) {
        this.id = id;
        this.pizza_name = pizza_name;
        this.picture = picture;
        this.description = description;
        this.rating = rating;
        this.price = price;
    }

    public Pizza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPizza_name() {
        return pizza_name;
    }

    public void setPizza_name(String pizza_name) {
        this.pizza_name = pizza_name;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
