package se.mozaffary.jsonxmlconverterapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import se.mozaffary.jsonxmlconverterapi.client.ApiClient;
import se.mozaffary.jsonxmlconverterapi.client.ApiResult;
import se.mozaffary.jsonxmlconverterapi.exeption.ExternalApiException;
import se.mozaffary.jsonxmlconverterapi.exeption.XmlConversionException;

import java.net.URI;

@Service
public class FetchJsonToXmlService {
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;
    XmlMapper xmlMapper = new XmlMapper();

    public FetchJsonToXmlService(ApiClient apiClient, ObjectMapper objectMapper){
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    public String jsonToXml(String url){
        try{
            ApiResult result = apiClient.fetchData(url);

            if (result.hasError()) {
                throw new ExternalApiException("Fel vid anrop mot extern API: " + result.getErrorMessage());
            }

            if (result.getResponse() == null) {
                throw new ExternalApiException("Ingen respons från extern API.");
            }

            if (result.getResponse().statusCode() != 200) {
                throw new ExternalApiException("Misslyckades att hämta data. Statuskod: " + result.getResponse().statusCode());
            }

            JsonNode jsonNode = objectMapper.readTree(result.getResponse().body());
            String rootName = guessRootNameFromUrl(url);
            String itemName = rootName.endsWith("s") ? rootName.substring(0, rootName.length() - 1) : "item";

            if (jsonNode.isArray()) {
                ObjectNode wrapper = objectMapper.createObjectNode();
                wrapper.set(itemName, jsonNode);
                return xmlMapper
                        .writerWithDefaultPrettyPrinter()
                        .withRootName(rootName)
                        .writeValueAsString(wrapper);

            } else {
                return xmlMapper
                        .writerWithDefaultPrettyPrinter()
                        .withRootName(itemName)
                        .writeValueAsString(jsonNode);
            }
        }catch (ExternalApiException e){
            throw  e;
        } catch (Exception e) {
            throw new XmlConversionException("Kunde inte konvertera JSON till XML: " + e.getMessage());
        }
    }

    private String guessRootNameFromUrl(String url) {
        String path = URI.create(url).getPath();

        String[] segments = path.split("/");
        for (int i = segments.length - 1; i >= 0; i--) {
            if (!segments[i].isBlank() && !segments[i].matches("\\d+")) {
                return segments[i];
            }
        }
        return "items";
    }
}
