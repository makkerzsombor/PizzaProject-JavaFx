package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.model.User;
import hu.pizza.pizzaproject.auth.JwtResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class UserRequests {

    public HttpResponse<String> updateUserRequest(User readyUser, long updateId, String USER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getJwttoken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Convert User to JSON string
        Gson converter = new Gson();
        String jsonBody = converter.toJson(readyUser);

        // Send the PUT request to "/users/{userId}"
        HttpResponse<String> response = null;
        try {
            response = requestHandler.sendPut(USER_URL + '/' + updateId, accessToken, jsonBody);
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
                    response = requestHandler.sendPut(USER_URL + updateId, accessToken, jsonBody);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }

        return response;
    }

    public User getUserInformation(String USER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getJwttoken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // GSON converter
        Gson converter = new Gson();

        // Send the GET request to "/data"
        HttpResponse<String> response = null;
        try {
            response = requestHandler.sendGet(USER_URL + "/data", accessToken);
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
                    // Retry the original request with the new access token
                    response = requestHandler.sendGet(USER_URL + "/data", accessToken);
                } catch (IOException | InterruptedException e) {
                    // Error
                    throw new RuntimeException(e);
                }
            }
        }

        // Parse the response body into a User object using Gson then return
        System.out.println(response.body());
        return converter.fromJson(response.body(), User.class);
    }


    public List<User> getallUserRequest(String BASE_URL){

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            HttpResponse<String> response = requestHandler.sendGetAll(BASE_URL + "/get-all");

            // Parse the response body into a List<User> object using Gson
            Type userListType = new TypeToken<List<User>>(){}.getType();
            userLista = converter.fromJson(response.body(), userListType);
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }
        // Lista visszaküldés
        return userLista;
    }

    public HttpResponse<String> deleteUserRequest(String USER_URL, long userId){
        // Tokenek kiolvasása
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getJwttoken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Send the PUT request to "/users/{userId}"
        HttpResponse<String> response = null;
        try{
            response = requestHandler.sendDelete(USER_URL + '/' + userId, accessToken);
        } catch (IOException | InterruptedException e){
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
                    response = requestHandler.sendDelete(USER_URL + userId, accessToken);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }

        return response;
    }
}
