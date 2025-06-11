package se.mozaffary.jsonxmlconverterapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.mozaffary.jsonxmlconverterapi.service.JsonToXmlService;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Konvertering", description = "Konvertera mellan JSON och XML-format")
public class JsonToXmlController {

    private final JsonToXmlService service;
    public JsonToXmlController(JsonToXmlService service){
        this.service = service;
    }

    @PostMapping("/json-to-xml")
    @Operation(
            summary = "Konvertera JSON till XML",
            description = "Tar emot en JSON-struktur och returnerar motsvarande XML-struktur."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Konvertering lyckades",
                    content = @Content(
                            schema = @Schema(example = "<item>\n" +
                                    "  <name>John Doe</name>\n" +
                                    "  <age>30</age>\n" +
                                    "  <city>Stockholm</city>\n" +
                                    "</item>")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Serverfel vid konvertering",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request body saknas eller är ogiltig.",
                    content = @Content
            )
    })
    public ResponseEntity<String> jsonToXml(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON som ska konverteras till XML",
                    required = true,
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = """
                                {
                                    "name": "John Doe",
                                    "age": 30,
                                     "city": "Stockholm"
                                }
                                """
                    ))
            )
            JsonNode jsonNode) {
        String xml = service.convertJsonToXml(jsonNode);
        return ResponseEntity.ok(xml);
    }
}
