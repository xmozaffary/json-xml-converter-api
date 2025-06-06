package se.mozaffary.jsonxmlconverterapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.mozaffary.jsonxmlconverterapi.client.ApiClient;
import se.mozaffary.jsonxmlconverterapi.client.ApiResult;
import se.mozaffary.jsonxmlconverterapi.dto.JsonUrlRequest;


import java.net.URI;


@RestController
@RequestMapping("/api/convert")
public class JsonToXmlController {

    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper = new XmlMapper();



    public JsonToXmlController(ApiClient apiClient, ObjectMapper objectMapper){
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    @Operation(
            summary = "Konvertera JSON från URL till XML",
            description = "Hämtar JSON-data från en extern URL och konverterar den till XML-format."
    )
    @ApiResponse(responseCode = "200", description = "Konvertering lyckades")
    @ApiResponse(responseCode = "400", description = "Felaktig förfrågan eller problem vid hämtning")
    @ApiResponse(responseCode = "500", description = "Serverfel vid konvertering")


    @PostMapping("/json-to-xml")
    public ResponseEntity<String> jsonToXml(@RequestBody JsonUrlRequest request){
        try{
            ApiResult result = apiClient.fetchData(request.getUrl());

            if (result.hasError()) {
                return ResponseEntity.status(400).body("Fel vid hämtning: " + result.getErrorMessage());
            }

            if (result.getResponse().statusCode() != 200 || result.getResponse().body() == null) {
                return ResponseEntity.status(result.getResponse().statusCode())
                        .body("Kunde inte hämta data: " + result.getResponse().body());
            }

            JsonNode jsonNode = objectMapper.readTree(result.getResponse().body());
            String rootName = guessRootNameFromUrl(request.getUrl());
            String itemName = rootName.endsWith("s") ? rootName.substring(0, rootName.length() - 1) : "item";

            if (jsonNode.isArray()) {
                ObjectNode wrapper = objectMapper.createObjectNode();
                wrapper.set(itemName, jsonNode);
                String xml = xmlMapper
                        .writerWithDefaultPrettyPrinter()
                        .withRootName(rootName)
                        .writeValueAsString(wrapper);

                return ResponseEntity.ok(xml);
            } else {
                String xml = xmlMapper
                        .writerWithDefaultPrettyPrinter()
                        .withRootName(itemName)
                        .writeValueAsString(jsonNode);
                return ResponseEntity.ok(xml);
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Ett oväntat fel inträffade.");
        }
    }


    private String guessRootNameFromUrl(String url) {
        String path = URI.create(url).getPath(); // t.ex. /users/1
        String[] segments = path.split("/");
        for (int i = segments.length - 1; i >= 0; i--) {
            if (!segments[i].isBlank() && !segments[i].matches("\\d+")) {
                return segments[i];
            }
        }
        return "items"; // fallback om inget bra namn hittas
    }

}
