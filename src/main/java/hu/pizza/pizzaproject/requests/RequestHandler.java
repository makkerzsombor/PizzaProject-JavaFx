package hu.pizza.pizzaproject.requests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHandler {
    /**
     * Httpkliens.
     */
    private final HttpClient httpClient;

    /**
     * Üres konstruktor a RequestHandler osztálynak.
     */
    public RequestHandler() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    /**
     * Az összes single GET requestet ez a funkció kezeli.
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @param accessToken Felhasználó accesstokenje biztonság érdekében meg kell adni, mivel a backedn leellenőrzi.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendGet(String url, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Ez a funkció az összes get-all(mindent lekérő) GET requesteket kezeli.
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @param accessToken Felhasználó accesstokenje biztonság érdekében meg kell adni, mivel a backedn leellenőrzi.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendGetAll(String url, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Ez a funkció .
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendGetAllPizzas(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Ez a funkció kezeli az összes POST requestet.
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @param accessToken Felhasználó accesstokenje biztonság érdekében meg kell adni, mivel a backedn leellenőrzi.
     * @param jsonBody Ez egy Json-á alakított osztály, amit JSON formában tudunk tovább adni a backend felé.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendPost(String url, String accessToken, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Ez a funkció kezeli az összes PUT requestet.
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @param accessToken Felhasználó accesstokenje biztonság érdekében meg kell adni, mivel a backedn leellenőrzi.
     * @param jsonBody Ez egy Json-á alakított osztály, amit JSON formában tudunk tovább adni a backend felé.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendPut(String url, String accessToken, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Ez a funkció kezeli az összes DELETE requestet.
     * @param url String mindig meg kell adni, hogy milyen linkre küldjön requestet.
     * @param accessToken Felhasználó accesstokenje biztonság érdekében meg kell adni, mivel a backedn leellenőrzi.
     * @return Http választ küld ad vissza a kérésnek megfelelően.
     * @throws IOException Amennyiben a program nem tudja beolvasni az adatainkat megfelelően IOException-t dob.
     * @throws InterruptedException Amennyiben a program hibásan fut le InterruptedException dob.
     */
    public HttpResponse<String> sendDelete(String url, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
