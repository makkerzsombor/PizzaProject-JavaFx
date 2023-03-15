package hu.pizza.pizzaproject.auth;

public abstract class ApplicationConfiguration {

    private static JwtResponse jwtResponse;

    public static JwtResponse getJwtResponse() {
        return jwtResponse;
    }

    public static void setJwtResponse(JwtResponse jwtResponse) {
        ApplicationConfiguration.jwtResponse = jwtResponse;
    }
}
