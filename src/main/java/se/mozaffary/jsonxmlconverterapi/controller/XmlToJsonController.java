package se.mozaffary.jsonxmlconverterapi.controller;

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
import se.mozaffary.jsonxmlconverterapi.service.XmlToJsonService;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Konvertering", description = "Konvertera mellan JSON och XML-format")
public class XmlToJsonController {
    private final XmlToJsonService service;

    public XmlToJsonController(XmlToJsonService service){
        this.service = service;
    }

    @PostMapping("/xml-to-json")
    @Operation(
            summary = "Konvertera XML till JSON",
            description = "Tar emot XML-struktur och returnerar motsvarande JSON-struktur."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Konvertering lyckades",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "name": "John Doe",
                                      "age": "30",
                                      "city": "Stockholm"
                                    }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Felaktig XML-struktur",
                    content = @Content
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
    public ResponseEntity<String> xmlToJson(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "XML som ska konverteras",
                    required = true,
                    content = @Content(
                            schema = @Schema(example = "<item>" +
                                    "<name>John Doe</name>" +
                                    "<age>30</age>"+
                                    "<city>Stockholm</city>"+
                                    "</item>")
                    )
            )
            String request){
        String xml = service.xmlToJson(request);
        return ResponseEntity.ok(xml);
    }
}
