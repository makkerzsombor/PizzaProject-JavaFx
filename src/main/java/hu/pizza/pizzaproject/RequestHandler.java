package hu.pizza.pizzaproject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.pizza.pizzaproject.Model.ApplicationConfiguration;
import hu.pizza.pizzaproject.dataClasses.Pizza;
import hu.pizza.pizzaproject.dataClasses.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {

    public Request addPizzaRequest(){
        Request request = new Request();
        return request;
    }
    public HttpResponse updateUserRequest(User readyUser, long updateId, String USER_URL){
        // GSON converter
        Gson converter = new Gson();

        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;

        // Jsonba átalakítás
        String jsonUser = converter.toJson(readyUser);
        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/" + updateId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }
    }

    public HttpResponse deleteRequest(String USER_URL, long selectedIndex){
        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;
        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/" + selectedIndex))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserRequest(String USER_URL){
        // JWT token kiolvasás
        String jsonToken = ApplicationConfiguration.getJwtToken().getJwtToken();

        // User létre hozása
        User user = new User();

        // GSON converter
        Gson converter = new Gson();

        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP Request
        HttpRequest dataRequest = null;

        try {
            // Prepare the request
            dataRequest = HttpRequest.newBuilder()
                    .uri(new URI(USER_URL + "/data"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + jsonToken)
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(dataRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response body into a User object using Gson
            System.out.println(response.body());
            user = converter.fromJson(response.body(), User.class);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }
        // User visszaadása:
        return user;
    }

    public List<User> getallUserRequest(String BASE_URL){
        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Lista<User>
        List<User> userLista = new ArrayList<User>();

        // HTTP Request
        HttpRequest usersrequest = null;

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            // Prepare the request
            usersrequest = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/get-all"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(usersrequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response body into a List<User> object using Gson
            Type userListType = new TypeToken<List<User>>(){}.getType();
            userLista = converter.fromJson(response.body(), userListType);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }
        // Lista visszaküldés
        return userLista;
    }

    public List<Pizza> getallPizzaRequest(String BASE_URL){
        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Lista<Pizza>
        List<Pizza> pizzaLista;

        // HTTP Request
        HttpRequest pizzaRequest = null;

        // Gson létrehozása (kiolvasáshoz)
        Gson converter = new Gson();

        try {
            // Prepare the request
            pizzaRequest = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/get-all"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(pizzaRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response body into a List<User> object using Gson
            Type pizzaListType = new TypeToken<List<Pizza>>(){}.getType();
            pizzaLista = converter.fromJson(response.body(), pizzaListType);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            // Error
            throw new RuntimeException(e);
        }
        // Lista visszaküldés
        return pizzaLista;
    }
}
