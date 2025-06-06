package se.mozaffary.jsonxmlconverterapi.client;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ApiClient {
    private final HttpClient client = HttpClient.newHttpClient();

    public ApiResult fetchData(String url){

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResult(response);

        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            return new ApiResult("IO-fel: " + e.getMessage());
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Interrupted: " + e.getMessage());
            return new ApiResult("Begäran avbröts: " + e.getMessage());
        }catch (IllegalArgumentException e){
            return new ApiResult("Ogiltig URL: " + e.getMessage());
        }
    }
}
