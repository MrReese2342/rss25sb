package fr.univrouen.rss25sb.controller;

import fr.univrouen.rss25sb.model.Item;
import fr.univrouen.rss25sb.model.ItemList;
import fr.univrouen.rss25sb.util.ErrorResponse;
import fr.univrouen.rss25sb.util.XMLValidator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rss25SB")
public class RssController {

    private final List<Item> items = new ArrayList<>();

    // Simule une base avec quelques articles
    public RssController() {
        items.add(new Item(1, "Titre 1", "Description 1", "http://exemple.com/1", new Date(), "category1"));
        items.add(new Item(2, "Titre 2", "Description 2", "http://exemple.com/2", new Date(), "category2"));
    }

    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ItemList getResumeXml() {
        // Filtrer pour ne retourner que id, date, guid
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            filteredItems.add(new Item(item.getId(), null, null, item.getLink(), item.getPubDate(), null));
        }
        return new ItemList(filteredItems);
    }

    @GetMapping(value = "/resume/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object getArticleById(@PathVariable int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item; // retour de l’article complet
            }
        }

        // Article non trouvé → message d'erreur XML
        return new ErrorResponse(id, "ERROR");
    }

    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Liste des articles</title></head><body>");
        sb.append("<h2>Liste des articles</h2>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>ID</th><th>Date</th><th>Lien</th></tr>");

        for (Item item : items) {
            sb.append("<tr>");
            sb.append("<td>").append(item.getId()).append("</td>");
            sb.append("<td>").append(item.getPubDate()).append("</td>");
            sb.append("<td><a href='").append(item.getLink()).append("'>").append(item.getLink()).append("</a></td>");
            sb.append("</tr>");
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }

    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getArticleHtmlById(@PathVariable int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return """
                <html>
                    <head><title>Article %d</title></head>
                    <body>
                        <h2>%s</h2>
                        <p><strong>Description :</strong> %s</p>
                        <p><strong>Lien :</strong> <a href="%s">%s</a></p>
                        <p><strong>Date de publication :</strong> %s</p>
                        <p><strong>Catégorie :</strong> %s</p>
                    </body>
                </html>
                """.formatted(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getLink(),
                        item.getLink(),
                        item.getPubDate(),
                        item.getCategory()
                );
            }
        }
        // Si pas trouvé
        return "<html><body><h2>Article non trouvé</h2></body></html>";
    }


    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public Object insertArticle(@RequestBody Item newItem) {
        if (!XMLValidator.validateXMLWithXSD(newItem)) {
            return new ErrorResponse(newItem.getId(), "XML invalide (non conforme au XSD)");
        }

        for (Item item : items) {
            if (item.getId() == newItem.getId()) {
                return new ErrorResponse(newItem.getId(), "Article déjà existant");
            }
        }

        items.add(newItem);
        return newItem;
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object deleteItemById(@PathVariable int id) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getId() == id) {
                iterator.remove();
                return item; // ou renvoie une réponse personnalisée
            }
        }
        return new ErrorResponse(id, "ERROR");
    }






}
