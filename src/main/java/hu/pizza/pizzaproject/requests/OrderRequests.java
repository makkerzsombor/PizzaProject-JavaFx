package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.pizza.pizzaproject.model.Order;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.auth.JwtResponse;
import hu.pizza.pizzaproject.model.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

public class OrderRequests {
    public List<Order> getallOrderRequest(String ORDER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Lista<Pizza>
        List<Order> orderLista;

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        HttpResponse<String> response = null;
        try {
             response = requestHandler.sendGetAll(ORDER_URL + "/get-new-orders", accessToken);

            // Parse the response body into a List<User> object using Gson
            Type orderListType = new TypeToken<List<Order>>(){}.getType();
            orderLista = converter.fromJson(response.body(), orderListType);
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }

        if (response.statusCode() == 451) {
            System.out.println("Access token expired, refreshing...");
            JwtResponse newJwtResponse = RefreshHandler.refresh(refreshToken);
            if (newJwtResponse != null) {
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the original request with the new access token
                    response = requestHandler.sendGet(ORDER_URL + "/data", accessToken);
                    Type orderListType = new TypeToken<List<Order>>(){}.getType();
                    orderLista = converter.fromJson(response.body(), orderListType);
                } catch (IOException | InterruptedException e) {
                    // Error
                    throw new RuntimeException(e);
                }
            }
        }

        // Lista visszaküldés
        return orderLista;
    }
    public HttpResponse<String> updateReadyStatus(Long updateId, String ORDER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        //ezJson = "ready": "true";
        Order readyOrder = new Order(updateId, true);

        // GSON converter
        Gson converter = new Gson();
        String jsonBody = converter.toJson(readyOrder);

        // Send the PUT request to "/order/{orderId}"
        HttpResponse<String> response = null;
        try {
            response = requestHandler.sendPut(ORDER_URL + '/' + updateId, accessToken, jsonBody);
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
                    response = requestHandler.sendPut(ORDER_URL + '/' + updateId, accessToken, jsonBody);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }

        return response;
    }
}
