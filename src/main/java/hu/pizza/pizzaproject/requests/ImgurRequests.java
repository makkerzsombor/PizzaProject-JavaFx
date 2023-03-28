package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hu.pizza.pizzaproject.auth.ApplicationConfiguration;
import hu.pizza.pizzaproject.auth.JwtResponse;
import hu.pizza.pizzaproject.model.FilePathAsString;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImgurRequests {
    private String clientId = "40d626f0cab2fa5";
    public String postImageToImgur(){
        // String alapértelmezetten ""
        String responseString = "";

        // Ez a kép előkészítés
        Path path = Path.of(FilePathAsString.getFilePath());
        byte[] imageData = null;
        try {
            imageData = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println("Nem okés valami a képpel!");
            throw new RuntimeException(e);
        }
        if (imageData == null){
            System.out.println("A kép null");
        }

        // Ez a http request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest uploadRequest = null;
        try {
            uploadRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.imgur.com/3/image"))
                    .header("Authorization", "Client-ID 40d626f0cab2fa5")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(imageData))
                    .build();

            HttpResponse<String> response = httpClient.send(uploadRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            if (response.statusCode() == 200){
                System.out.println("sikeres feltöltés");
                // Parse the response body to get the link
                JsonObject jsonResponse = new Gson().fromJson(response.body(), JsonObject.class);
                String link = jsonResponse.getAsJsonObject("data").get("link").getAsString();
                System.out.println("Link: " + link);
                responseString = link;
            }else {
                System.out.println(response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return responseString;
    }
}
