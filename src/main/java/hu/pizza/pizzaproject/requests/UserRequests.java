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

/**
 * Felhasználókérés osztály.
 */
public class UserRequests {
    /**
     * Ez a funkció kezeli a felhasználók módosítását PUT request.
     * @param readyUser Az éppen módosítandó tulajdonságai(id nélkül).
     * @param updateId Az éppen módosítandó felhasználó id-je.
     * @param USER_URL String link as alap felhasználói endpointokhoz "http://localhost:8080/user".
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     */
    public HttpResponse<String> updateUserRequest(User readyUser, Long updateId, String USER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
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
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the PUT request with the new access token
                    response = requestHandler.sendPut(USER_URL + '/' + updateId, accessToken, jsonBody);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }
        return response;
    }

    /**
     * A belépett felhasználó adatait lekérő GET request.
     * @param USER_URL String link as alap felhasználói endpointokhoz "http://localhost:8080/user".
     * @return A belépett felhasználó felhasználó típusú adatait küldi vissza.
     */
    public User getUserInformation(String USER_URL) {
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
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
                accessToken = newJwtResponse.getAccessToken();
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

    /**
     * Az összes adatbázisban megtalálható felhasználót lekérő funkció GET request.
     * @param USER_URL USER_URL String link as alap felhasználói endpointokhoz "http://localhost:8080/user".
     * @return Felhasználókból álló listát ad vissza.
     */
    public List<User> getallUserRequest(String USER_URL){
        // Get access and refresh tokens
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
        String refreshToken = jwtResponse.getRefreshToken();

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler();

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        HttpResponse<String> response = null;
        try {
            response = requestHandler.sendGetAll(USER_URL + "/get-all", accessToken);

            // Parse the response body into a List<User> object using Gson
            Type userListType = new TypeToken<List<User>>(){}.getType();
            userLista = converter.fromJson(response.body(), userListType);
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }
        // Lista visszaküldés

        // If the response status code is 451, call refreshHandler and retry the request
        if (response.statusCode() == 451) {
            System.out.println("Access token expired, refreshing...");
            JwtResponse newJwtResponse = RefreshHandler.refresh(refreshToken);
            if (newJwtResponse != null) {
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the original request with the new access token
                    response = requestHandler.sendGet(USER_URL + "/get-all", accessToken);
                    Type userListType = new TypeToken<List<User>>(){}.getType();
                    userLista = converter.fromJson(response.body(), userListType);
                } catch (IOException | InterruptedException e) {
                    // Error
                    throw new RuntimeException(e);
                }
            }
        }
        return userLista;
    }

    /**
     * Egy adott felhasználó törlésére szolgáló DELETE request.
     * @param USER_URL USER_URL USER_URL String link as alap felhasználói endpointokhoz "http://localhost:8080/user".
     * @param userId A törlendő felhasználó id-je.
     * @return A törlés sikereségéről küld Http választ vissza.
     */
    public HttpResponse<String> deleteUserRequest(String USER_URL, long userId){
        // Tokenek kiolvasása
        JwtResponse jwtResponse = ApplicationConfiguration.getJwtResponse();
        String accessToken = jwtResponse.getAccessToken();
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
                accessToken = newJwtResponse.getAccessToken();
                ApplicationConfiguration.setJwtResponse(newJwtResponse);
                try {
                    // Retry the PUT request with the new access token
                    response = requestHandler.sendDelete(USER_URL + '/' + userId, accessToken);
                } catch (IOException | InterruptedException ex) {
                    // Error
                    throw new RuntimeException(ex);
                }
            }
        }
        return response;
    }
}
