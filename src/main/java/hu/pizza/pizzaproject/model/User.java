package hu.pizza.pizzaproject.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    /**
     * Felhasználó id.
     */
    private Long id;
    /**
     * Felhasználó Keresztneve.
     */
    private String first_name;
    /**
     * Felhasználó Vezetékneve.
     */
    private String last_name;
    /**
     * Felhasználó Email címe.
     */
    private String email;
    /**
     * Felhasználó Jelszava.
     */
    private String password = null;
    /**
     * Felhasználó szerepe(ADMIN/USER).
     */
    private String role;

    /**
     * Felhasználó konstruktorja jelszó nélkül.
     * @param id Új felhasználó id-je.
     * @param first_name Új felhasználó keresztneve.
     * @param last_name Új felhasználó vezetékneve.
     * @param email Új felhasználó email-je.
     * @param admin Új felhasználó szerepe-je.
     */

    public User(Long id, String first_name, String last_name, String email, String admin) {
        this.id = id;
        this.email = email;
        this.role = admin;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    /**
     * Felhasználó konstruktorja belépéshez email + jelszóval.
     * @param email Felhasználó emailje.
     * @param password Felhasználó jelszava.
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Felhasználó szerepét meghatározó funkció.
     * @return String, amiből megállíphatjuk, hogy a felhasználó milyen jogokkal rendelkezik.
     */
    public String getAdmin() {
        return role;
    }
}
