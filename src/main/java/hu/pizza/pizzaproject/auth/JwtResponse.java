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
    /**
     * AccessToken belépéskor ezt a tokent kapjuk meg és ezzel tudunk minden kezelni a kéréseket/változtatásokat.
     * 5 perc után lejár, miután lejár a refresh tokenből felhasználásával kérünk újat.
     */
    private String accessToken;
    /**
     * RefreshToken 30 naping érvényes ezzel a tokennel frissítjük a lejárt accessTokent.
     */
    private String refreshToken;
}
