package se.mozaffary.jsonxmlconverterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.mozaffary.jsonxmlconverterapi.dto.JsonUrlRequest;
import se.mozaffary.jsonxmlconverterapi.service.FetchJsonToXmlService;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Fetch konvertering", description = "Konvertering mellan JSON och XML från extern källa")
public class FetchJsonToXmlController {

    private final FetchJsonToXmlService service;

    public FetchJsonToXmlController(FetchJsonToXmlService service){
        this.service = service;
    }


    @PostMapping("/fetch-json-to-xml")
    @Operation(
            summary = "Hämta JSON från extern URL och konvertera till XML",
            description = "Denna endpoint hämtar JSON från en angiven `URL` och konverterar det till XML. Rotnodens namn baseras på innehållet eller URL:en."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Konvertering lyckades",
                    content = @Content(
                            schema = @Schema(example = """
                            <?xml version=1.0 encoding=UTF-8?>
                            <users>
                              <user>
                                <id>1</id>
                                <name>Leanne Graham</name>
                                <username>Bret</username>
                                <email>Sincere@april.biz</email>
                                <address>
                                  <street>Kulas Light</street>
                                  <suite>Apt. 556</suite>
                                  <city>Gwenborough</city>
                                  <zipcode>92998-3874</zipcode>
                                  <geo>
                                    <lat>-37.3159</lat>
                                    <lng>81.1496</lng>
                                  </geo>
                                </address>
                                <phone>1-770-736-8031 x56442</phone>
                                <website>hildegard.org</website>
                                <company>
                                  <name>Romaguera-Crona</name>
                                  <catchPhrase>Multi-layered client-server neural-net</catchPhrase>
                                  <bs>harness real-time e-markets</bs>
                                </company>
                              </user>
                            </users>""")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request body saknas eller är ogiltig.",
                    content = @Content

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Serverfel vid konvertering",
                    content = @Content

            )
    })
    public ResponseEntity<String> jsonToXml(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "En JSON-body som innehåller en `url` till den externa JSON-resursen.",
                    required = true,
                    content = @Content(schema = @Schema(
                            implementation = JsonUrlRequest.class,
                            example = """
                                {
                                  "url": "https://jsonplaceholder.typicode.com/users"
                                }
                                """
                    ))
            )
            JsonUrlRequest request){
        return ResponseEntity.ok(service.jsonToXml(request.getUrl()));
    }
}
