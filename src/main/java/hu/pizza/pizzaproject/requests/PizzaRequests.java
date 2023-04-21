package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.pizza.pizzaproject.model.Pizza;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.auth.JwtResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Pizzakérés osztály.
 */
public class PizzaRequests {
    /**
     * Új pizza létrehozására szolgáló POST request.
     * @param PIZZA_URL String "http://localhost:8080/pizza" alap pizza műveletekhez szükséges.
     * @param newPizza A formból kiszedett Pizza típusú változó(Ez kerül feltöltésre).
     * @return A pizza létrehozásáról kapott választ küldi nekünk vissza.
     */
    public HttpResponse<String> addPizzaRequest(String PIZZA_URL, Pizza newPizza) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // GSON converter
        Gson converter = new Gson();
        String jsonBody = converter.toJson(newPizza);

        // Send the POST request to "/pizza/add-pizza"
        HttpResponse<String> response;
        try {
            response = requestHandler.sendPost(PIZZA_URL + "/add-pizza", accessToken, jsonBody);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // If the response status code is 451, call refreshHandler and retry the request
        if (response.statusCode() == 451) {
            System.out.println("Access token expired, refreshing...");
            JwtResponse newJwtResponse = RefreshHandler.refresh(refreshToken);
            if (newJwtResponse != null) {
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the PUT request with the new access token
                    response = requestHandler.sendPost(PIZZA_URL + "/add-pizza", accessToken, jsonBody);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }

        return response;
    }

    /**
     * Pizza módosítására szolgáló PUT request.
     * @param readyPizza A módosított pizza adatai(id nélkül, mert azt nem akarjuk változtatni).
     * @param updateId A módosított pizza id-je.
     * @param PIZZA_URL String "http://localhost:8080/pizza" alap pizza műveletekhez szükséges.
     * @return A pizza módosításáról kapott választ küldi nekünk vissza.
     */
    public HttpResponse<String> updatePizzaRequest(Pizza readyPizza, Long updateId, String PIZZA_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // GSON converter
        Gson converter = new Gson();
        String jsonBody = converter.toJson(readyPizza);

        // Send the PUT request to "/users/{userId}"
        HttpResponse<String> response;
        try {
            response = requestHandler.sendPut(PIZZA_URL + '/' + updateId, accessToken, jsonBody);
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }

        // If the response status code is 451, call refreshHandler and retry the request
        if (response.statusCode() == 451) {
            System.out.println("Access token expired, refreshing...");
            JwtResponse newJwtResponse = RefreshHandler.refresh(refreshToken);
            if (newJwtResponse != null) {
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the PUT request with the new access token
                    response = requestHandler.sendPut(PIZZA_URL + '/' + updateId, accessToken, jsonBody);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }

        return response;
    }

    /**
     * Lekéri az összes pizzát az adatbázisból a backend "http://localhost:8080/pizza/get-all" végpontját használva GET request.
     * @param BASE_URL String "http://localhost:8080/pizza" alap pizza műveletekhez szükséges.
     * @return Pizza típusú pizzákból álló listát ad vissza.
     */
    public List<Pizza> getAllPizzaRequest(String BASE_URL) {

        // Lista<Pizza>
        List<Pizza> pizzaLista;

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            HttpResponse<String> response = requestHandler.sendGetAllPizzas(BASE_URL + "/get-all");

            // Parse the response body into a List<User> object using Gson
            Type pizzaListType = new TypeToken<List<Pizza>>() {
            }.getType();
            pizzaLista = converter.fromJson(response.body(), pizzaListType);
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }
        // Lista visszaküldés
        return pizzaLista;
    }
}
