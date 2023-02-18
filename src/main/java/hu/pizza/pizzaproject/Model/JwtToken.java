package hu.pizza.pizzaproject.Model;

public class JwtToken {
    private String jwttoken;

    public String getJwtToken() {
        return jwttoken;
    }

    public void setJwtToken(String jwtToket) {
        this.jwttoken = jwtToket;
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "jwttoken='" + jwttoken + '\'' +
                '}';
    }
}
