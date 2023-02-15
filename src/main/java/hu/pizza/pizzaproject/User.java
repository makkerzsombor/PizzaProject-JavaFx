package hu.pizza.pizzaproject;

public class User {
    private int id;
    private String email;
    private String password;
    private boolean admin;
    private String first_name;
    private String last_name;

    public User(int id, String email, String password, boolean admin, String first_name, String last_name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
