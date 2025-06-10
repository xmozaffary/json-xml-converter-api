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
import se.mozaffary.jsonxmlconverterapi.dto.XmlUrlRequest;
import se.mozaffary.jsonxmlconverterapi.service.FetchXmlToJsonService;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Fetch konvertering", description = "Konvertera mellan XML och JSON från extern URL")

public class FetchXmlToJsonController {

    private final FetchXmlToJsonService service;

    public FetchXmlToJsonController(FetchXmlToJsonService service){
        this.service = service;
    }
    @PostMapping("/fetch-xml-to-json")
    @Operation(
            summary = "Hämta XML från extern URL och konvertera till JSON",
            description = "Denna endpoint hämtar XML-data från en extern URL (angiven som `url` i JSON-body) och returnerar den som JSON."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Konvertering lyckades",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                {
                                    "to": "Tove",
                                    "from": "Jani",
                                    "heading": "Reminder",
                                    "body": "Don't forget me this weekend!"
                                }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ogiltig URL eller felaktig XML-data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Serverfel vid konvertering",
                    content = @Content
            )
    })
    public ResponseEntity<String> xmlToJson(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "En JSON med fältet `url`, som är adressen till den externa XML-resursen.",
                    required = true,
                    content = @Content(schema = @Schema(
                            implementation = XmlUrlRequest.class,
                            example = """
                                {
                                  "url": "https://www.w3schools.com/xml/note.xml"
                                }
                                """
                    ))
            )
            XmlUrlRequest request) {
        return ResponseEntity.ok(service.xmlToJson(request.getUrl()));
    }
}
