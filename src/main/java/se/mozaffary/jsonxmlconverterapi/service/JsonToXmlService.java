package se.mozaffary.jsonxmlconverterapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.stereotype.Service;
import se.mozaffary.jsonxmlconverterapi.exeption.XmlConversionException;

@Service

public class JsonToXmlService {
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    public JsonToXmlService() {
        this.objectMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }


    public String convertJsonToXml(JsonNode jsonNode) {
        try{
            String rootName = jsonNode.isArray() ? "items" : "item";
            JsonNode wrappedNode = jsonNode;
            if (jsonNode.isArray()) {
                ObjectNode wrapper = objectMapper.createObjectNode();
                wrapper.set("item", jsonNode);
                wrappedNode = wrapper;
            }
            return xmlMapper
                    .writerWithDefaultPrettyPrinter()
                    .withRootName(rootName)
                    .writeValueAsString(wrappedNode);

        }catch (Exception e){
            throw new XmlConversionException("Kunde inte konvertera JSON till XML: " + e.getMessage());
        }
    }
}