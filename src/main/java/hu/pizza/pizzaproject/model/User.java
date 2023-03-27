package hu.pizza.pizzaproject.model;

public class User {
    private Long id;
    private String email;
    private String password = null;
    private String role;
    private String first_name;
    private String last_name;

    public User(Long id, String first_name, String last_name, String email, String password, String admin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = admin;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public User(Long id, String first_name, String last_name, String email, String admin) {
        this.id = id;
        this.email = email;
        this.role = admin;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAdmin() {
        return role;
    }

    public void setAdmin(String admin) {
        this.role = admin;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + role +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
