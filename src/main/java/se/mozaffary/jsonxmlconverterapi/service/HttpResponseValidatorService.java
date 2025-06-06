package se.mozaffary.jsonxmlconverterapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class HttpResponseValidatorService {

    public ResponseEntity<String> validateResponse(HttpResponse<String> response) {
        if (response == null) {
            System.err.println("Fel: Ingen respons (null)");
            return ResponseEntity.status(502)
                    .body("Tjänsten svarar inte. Försök igen senare.");
        }

        if (response.statusCode() != 200) {
            System.err.println("Felaktig statuskod: " + response.statusCode());
            return ResponseEntity.status(response.statusCode())
                    .body("Kunde inte hämta data från angiven URL.");
        }

        if (response.body() == null || response.body().isBlank()) {
            System.err.println("Svar har tom body");
            return ResponseEntity.status(502)
                    .body("Tjänsten returnerade tomt svar.");
        }

        // Allt OK
        return null;
    }
}
