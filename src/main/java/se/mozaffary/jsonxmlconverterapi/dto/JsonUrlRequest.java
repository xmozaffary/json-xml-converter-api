package se.mozaffary.jsonxmlconverterapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class JsonUrlRequest {
    @Schema(description = "URL till JSON-källan", example = "https://jsonplaceholder.typicode.com/users")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
