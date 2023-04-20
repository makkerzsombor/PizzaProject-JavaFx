package hu.pizza.pizzaproject.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password = null;
    private String role;

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

    public String getAdmin() {
        return role;
    }
}
