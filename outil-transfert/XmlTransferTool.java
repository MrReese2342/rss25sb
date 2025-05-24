import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class XmlTransferTool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Outil de transfert rss25SB ===");
        System.out.print("→ URL du serveur (ex: http://localhost): ");
        String serverUrl = scanner.nextLine().trim();

        System.out.print("→ Port (ex: 8080): ");
        String port = scanner.nextLine().trim();

        System.out.print("→ Chemin absolu du fichier XML : ");
        String filePath = scanner.nextLine().trim();

        String xmlContent;
        try {
            xmlContent = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("❌ Erreur de lecture du fichier : " + e.getMessage());
            return;
        }

        String endpoint = serverUrl + ":" + port + "/rss25SB/insert";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(xmlContent))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Réponse du serveur (" + response.statusCode() + "):");
            System.out.println(response.body());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’envoi : " + e.getMessage());
        }
    }
}
