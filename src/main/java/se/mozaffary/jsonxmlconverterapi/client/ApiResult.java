package se.mozaffary.jsonxmlconverterapi.client;

import java.net.http.HttpResponse;

public class ApiResult {
    private final HttpResponse<String> response;
    private final String errorMessage;

    public ApiResult(HttpResponse<String> response) {
        this.response = response;
        this.errorMessage = null;
    }

    public ApiResult(String errorMessage) {
        this.response = null;
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
