package fr.univrouen.rss25sb.controller;

import fr.univrouen.rss25sb.model.Item;
import fr.univrouen.rss25sb.model.ItemList;
import fr.univrouen.rss25sb.repository.ItemRepository;
import fr.univrouen.rss25sb.util.ErrorResponse;
import fr.univrouen.rss25sb.util.XMLValidator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rss25SB")
public class RssController {

    private final ItemRepository itemRepository;

    public RssController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Résumé XML
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ItemList getResumeXml() {
        List<Item> filtered = itemRepository.findAll().stream()
                .map(item -> new Item(item.getId(), null, null, item.getLink(), item.getPubDate(), null))
                .collect(Collectors.toList());
        return new ItemList(filtered);
    }

    // Détail XML
    @GetMapping(value = "/resume/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object getArticleById(@PathVariable int id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            return new ErrorResponse(id, "ERROR", "Article non trouvé.");
        }
        return item;
    }

    // Résumé HTML
    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Liste des articles</title></head><body>");
        sb.append("<h2>Liste des articles</h2><table border='1'>");
        sb.append("<tr><th>ID</th><th>Date</th><th>Lien</th></tr>");

        for (Item item : itemRepository.findAll()) {
            sb.append("<tr>")
                    .append("<td>").append(item.getId()).append("</td>")
                    .append("<td>").append(item.getPubDate()).append("</td>")
                    .append("<td><a href='").append(item.getLink()).append("'>")
                    .append(item.getLink()).append("</a></td>")
                    .append("</tr>");
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }

    // Détail HTML
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getArticleHtmlById(@PathVariable int id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            return "<html><body><h2>Article non trouvé</h2></body></html>";
        }

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

    // Insertion XML
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public Object insertArticle(@RequestBody Item newItem) {
        if (!XMLValidator.validateXMLWithXSD(newItem)) {
            return new ErrorResponse(
                    newItem.getId(),
                    "XML invalide",
                    XMLValidator.getLastErrorMessage()
            );
        }

        if (itemRepository.existsById(newItem.getId())) {
            return new ErrorResponse(
                    newItem.getId(),
                    "Article déjà existant",
                    "Un article avec cet ID existe déjà."
            );
        }

        itemRepository.save(newItem);
        return newItem;
    }

    // Suppression XML
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object deleteItemById(@PathVariable int id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            return new ErrorResponse(id, "ERROR", "Article non trouvé.");
        }

        itemRepository.deleteById(item.getDbId());
        return item;
    }

    // Bonus : Recherche par date et catégorie
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_XML_VALUE)
    public ItemList search(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String category) {
        List<Item> results = itemRepository.findAll().stream()
                .filter(item -> (date == null || item.getPubDate().toString().contains(date)))
                .filter(item -> (category == null || item.getCategory().equalsIgnoreCase(category)))
                .collect(Collectors.toList());
        return new ItemList(results);
    }
}
