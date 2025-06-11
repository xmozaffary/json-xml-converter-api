package se.mozaffary.jsonxmlconverterapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import se.mozaffary.jsonxmlconverterapi.exeption.XmlConversionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Service
public class XmlToJsonService {
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    public XmlToJsonService(ObjectMapper objectMapper) throws Exception {
        this.objectMapper = objectMapper;
        this.xmlMapper = new XmlMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    public String xmlToJson(String xml) {
        try {
            if(!isValidXml(xml)){
                throw new XmlConversionException("Felaktig xml-struktur");
            }
            Object xmlObject = xmlMapper.readValue(xml, Object.class);
            return objectMapper.writeValueAsString(xmlObject);

        } catch (Exception e) {
            throw new XmlConversionException("Kunde inte konvertera XML till JSON: " + e.getMessage());
        }
    }

    public boolean isValidXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xml)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
