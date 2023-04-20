package hu.pizza.pizzaproject.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
