package fr.univrouen.rss25sb.converter;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class XmlConverterTool {

    // Exemple de sources RSS prédéfinies
    private static final String[][] SOURCES = {
            {"Le Monde - Médias", "https://www.lemonde.fr/actualite-medias/rss_full.xml"},
            {"Gouvernement - Actualités", "https://www.fonction-publique.gouv.fr/flux-rss-actualites"}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Outil de conversion vers rss25SB ===");
        System.out.println("Sources disponibles :");

        for (int i = 0; i < SOURCES.length; i++) {
            System.out.printf("%d. %s%n", i + 1, SOURCES[i][0]);
        }

        System.out.print("Choisissez une source (1-" + SOURCES.length + ") : ");
        int choix = Integer.parseInt(scanner.nextLine()) - 1;
        String sourceName = SOURCES[choix][0];
        String sourceUrl = SOURCES[choix][1];

        System.out.println("Téléchargement du flux : " + sourceName);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(sourceUrl)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Échec du téléchargement : " + response.statusCode());
                return;
            }

            String xmlContent = response.body();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document sourceDoc = dBuilder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

            NodeList items = sourceDoc.getElementsByTagName("item");

            Document newDoc = dBuilder.newDocument();
            Element root = newDoc.createElement("rss25SB");
            newDoc.appendChild(root);

            int id = 1000;
            for (int i = 0; i < items.getLength(); i++) {
                Node itemNode = items.item(i);
                if (itemNode.getNodeType() != Node.ELEMENT_NODE) continue;

                Element sourceItem = (Element) itemNode;
                Element newItem = newDoc.createElement("item");

                // ID auto-généré
                newItem.setAttribute("id", String.valueOf(id++));

                // title
                String title = getText(sourceItem, "title");
                appendText(newDoc, newItem, "title", title);

                // description
                String desc = getText(sourceItem, "description");
                appendText(newDoc, newItem, "description", desc);

                // link
                String link = getText(sourceItem, "link");
                appendText(newDoc, newItem, "link", link);

                // pubDate
                String date = getText(sourceItem, "pubDate");
                appendText(newDoc, newItem, "pubDate", date);

                // category (valeur par défaut si manquante)
                String cat = getText(sourceItem, "category");
                appendText(newDoc, newItem, "category", cat != null ? cat : "générique");

                root.appendChild(newItem);
            }

            String outputPath = "converted-rss25SB.xml";
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(newDoc);
            StreamResult streamResult = new StreamResult(new File(outputPath));
            transformer.transform(domSource, streamResult);

            System.out.println("Conversion terminée. Fichier généré : " + outputPath);

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    private static String getText(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        return list.item(0).getTextContent();
    }

    private static void appendText(Document doc, Element parent, String tag, String value) {
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(value != null ? value : ""));
        parent.appendChild(elem);
    }
}
