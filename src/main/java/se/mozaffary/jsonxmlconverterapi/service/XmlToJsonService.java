package se.mozaffary.jsonxmlconverterapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import se.mozaffary.jsonxmlconverterapi.exeption.XmlConversionException;

@Service
public class XmlToJsonService {
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public XmlToJsonService(ObjectMapper objectMapper) throws Exception {
        this.objectMapper = objectMapper;
        this.xmlMapper = new XmlMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    public String xmlToJson(String url) {
        try {
            Object xmlObject = xmlMapper.readValue(url, Object.class);
            return objectMapper.writeValueAsString(xmlObject);

        } catch (Exception e) {
            throw new XmlConversionException("Kunde inte konvertera XML till JSON: " + e.getMessage());
        }
    }
}
