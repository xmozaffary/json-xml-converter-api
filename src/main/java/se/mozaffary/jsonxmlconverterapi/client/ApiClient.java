package se.mozaffary.jsonxmlconverterapi.client;

import org.springframework.stereotype.Component;
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

        }catch (Exception e){
            return new ApiResult("IO-fel: " + e.getMessage());
        }
    }
}
