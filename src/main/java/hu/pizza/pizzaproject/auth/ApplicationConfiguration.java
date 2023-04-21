package hu.pizza.pizzaproject.auth;

/**
 * ApplikációKonfigurációs osztály.
 */
public abstract class ApplicationConfiguration {
    /**
     * Akárhonna lekérhető JWTResponse típusú változó.
     */
    private static JwtResponse jwtResponse;

    /**
     * Ezt a funkciót hívjuk meg, amikor le szeretnénk hívni a JWTtokenünk.
     * @return JWTResponse típusú változót ad vissza.
     */
    public static JwtResponse getJwtResponse() {
        return jwtResponse;
    }

    /**
     * Ezzel a funkcióval tudjuk a JWTTokent változtatni.
     * @param jwtResponse Stringet vár, ami az új JWTTokenünk lesz.
     */
    public static void setJwtResponse(JwtResponse jwtResponse) {
        ApplicationConfiguration.jwtResponse = jwtResponse;
    }
}
