package fr.univrouen.rss25sb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return """
            <html>
                <head><title>rss25SB - Accueil</title></head>
                <body>
                    <h1>Projet rss25SB</h1>
                    <p>Version : 1.0</p>
                    <p>Développeurs : Abderrahmane EL HATHOUT, Mahad MOUSSA ABDILLAHI</p>
                    <img src='https://lettresmodernes.univ-rouen.fr/wp-content/uploads/2019/09/logo-univ-rouen-normandie-couleur.png' width='150'/>
                </body>
            </html>
        """;
    }

    @GetMapping("/help")
    public String help() {
        return """
            <html>
                <head><title>Aide rss25SB</title></head>
                <body>
                    <h2>Aide du service REST</h2>
                    <ul>
                        <li>GET /rss25SB/resume/xml : liste des articles (XML)</li>
                        <li>GET /rss25SB/resume/html : liste des articles (HTML)</li>
                        <li>GET /rss25SB/resume/xml/{id} : article complet (XML)</li>
                        <li>GET /rss25SB/html/{id} : article complet (HTML)</li>
                        <li>POST /rss25SB/insert : insertion d’un article (XML)</li>
                        <li>DELETE /rss25SB/delete/{id} : suppression d’un article</li>
                    </ul>
                </body>
            </html>
        """;
    }
}
