package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import hu.pizza.pizzaproject.auth.JwtResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RefreshHandler {
    public static JwtResponse refresh(String refreshToken) {
        // HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Prepare the request
        HttpRequest refreshRequest;
        try {
            refreshRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/auth/refresh"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"refreshToken\":\"" + refreshToken + "\"}"))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Send the request and get the response
        try {
            HttpResponse<String> response = httpClient.send(refreshRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse the response body into a JwtResponse object using Gson
                Gson converter = new Gson();
                return converter.fromJson(response.body(), JwtResponse.class);
            } else {
                System.out.println("Refresh token expired or invalid.");
                return null;
            }
        } catch (IOException | InterruptedException e) {
            // Error
            throw new RuntimeException(e);
        }
    }

}
