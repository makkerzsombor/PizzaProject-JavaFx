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

public class PizzaRequests {
    public HttpResponse<String> addPizzaRequest(String PIZZA_URL, Pizza newPizza) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getJwttoken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // GSON converter
        Gson converter = new Gson();
        String jsonBody = converter.toJson(newPizza);

        // Send the POST request to "/pizza/add-pizza"
        HttpResponse<String> response = null;
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
                accessToken = newJwtResponse.getJwttoken();
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

    public HttpResponse<String> updatePizzaRequest(Pizza readyPizza, int updateId, String PIZZA_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getJwttoken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // GSON converter
        Gson converter = new Gson();
        String jsonBody = converter.toJson(readyPizza);

        // Send the PUT request to "/users/{userId}"
        HttpResponse<String> response = null;
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
                accessToken = newJwtResponse.getJwttoken();
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

    public List<Pizza> getAllPizzaRequest(String BASE_URL) {

        // Lista<Pizza>
        List<Pizza> pizzaLista;

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            HttpResponse<String> response = requestHandler.sendGetAll(BASE_URL + "/get-all");

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
