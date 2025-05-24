package fr.univrouen.rss25sb.util;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse {

    @XmlElement
    private int id;

    @XmlElement
    private String status;

    @XmlElement
    private String description; // ✅ Ajout de la description de l’erreur

    // Constructeur vide requis pour JAXB
    public ErrorResponse() {}

    // Constructeur avec description
    public ErrorResponse(int id, String status, String description) {
        this.id = id;
        this.status = status;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    // Setters (optionnels si non utilisés)
    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
