package se.mozaffary.jsonxmlconverterapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import se.mozaffary.jsonxmlconverterapi.client.ApiClient;
import se.mozaffary.jsonxmlconverterapi.client.ApiResult;
import se.mozaffary.jsonxmlconverterapi.exeption.ExternalApiException;
import se.mozaffary.jsonxmlconverterapi.exeption.XmlConversionException;

@Service
public class FetchXmlToJsonService {

    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;
    XmlMapper xmlMapper = new XmlMapper();


    public FetchXmlToJsonService(ApiClient apiClient, ObjectMapper objectMapper){
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }


    public String xmlToJson(String url){
        try {

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

            String xmlData = result.getResponse().body();
            Object xmlObject = xmlMapper.readValue(xmlData, Object.class);

            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            return objectMapper.writeValueAsString(xmlObject);

        }catch (ExternalApiException e){
          throw  e;
        } catch (Exception e) {
            throw new XmlConversionException("Kunde inte konvertera XML till JSON: " + e.getMessage());
        }
    }
}
