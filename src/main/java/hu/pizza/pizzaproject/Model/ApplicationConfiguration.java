package hu.pizza.pizzaproject.Model;
public abstract class ApplicationConfiguration {

    private static JwtToken jwtToken;

    public static JwtToken getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(JwtToken jwtToken) {
        ApplicationConfiguration.jwtToken = jwtToken;
    }
}
