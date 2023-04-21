package hu.pizza.pizzaproject.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hu.pizza.pizzaproject.model.FilePathAsString;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ImgurKérés osztály.
 */
public class ImgurRequests {
    /**
     * Imgur API-ra kép feltöltés POST requestje.
     * Ellenőrzi, hogy a kép nem nagyobb, mint 10MB.
     * @return Response Státuszkódot ad vissza.
     */
    public String postImageToImgur(){
        // String alapértelmezetten ""
        String responseString = "";

        // Ez a kép előkészítés
        Path path = Path.of(FilePathAsString.getFilePath());
        byte[] imageData;
        try {
            imageData = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println("Nem okés valami a képpel!");
            throw new RuntimeException(e);
        }

        if (imageData.length > 10_000_000) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("A kép mérete túl nagy (Max 10MB)!");
                alert.showAndWait();
            });
            throw new RuntimeException();
        }

        // Ez a http request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest uploadRequest;
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
