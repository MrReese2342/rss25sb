package fr.univrouen.rss25sb.util;

import fr.univrouen.rss25sb.model.Item;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.core.io.ClassPathResource;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLValidator {

    public static boolean validateXMLWithXSD(Item item) {
        try {
            // Convertir l'objet Java en XML
            JAXBContext context = JAXBContext.newInstance(Item.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(item, xmlWriter);
            String xml = xmlWriter.toString();

            // Charger le schéma XSD depuis les ressources
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new ClassPathResource("rss25.xsd").getInputStream()));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
