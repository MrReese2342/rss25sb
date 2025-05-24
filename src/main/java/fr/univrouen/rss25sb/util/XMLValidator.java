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

/**
 * Classe utilitaire pour valider un objet Item en XML avec un schéma XSD
 * et fournir le message d'erreur en cas d'échec.
 */
public class XMLValidator {

    // Variable qui contiendra le dernier message d'erreur rencontré
    private static String lastErrorMessage = null;

    /**
     * Valide un objet Item contre un schéma XSD.
     * @param item l'objet à valider
     * @return true si valide, false sinon
     */
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

            lastErrorMessage = null;
            return true;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            return false;
        }
    }

    /**
     * Récupère le dernier message d'erreur de validation XML.
     * @return message ou null si pas d'erreur
     */
    public static String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
