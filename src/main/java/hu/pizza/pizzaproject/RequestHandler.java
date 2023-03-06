package hu.pizza.pizzaproject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RequestHandler {

    public Request addRequest(){
        Request request = new Request();
        return request;
    }
    public Request updateRequest(){
        Request request = new Request();
        return request;
    }

    public Request deleterequest(){
        Request request = new Request();
        return request;
    }



}
