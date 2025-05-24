package fr.univrouen.rss25sb.util;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "error")
public class ErrorResponse {

    @XmlElement
    private int id;

    @XmlElement
    private String status;

    public ErrorResponse() {
        // Constructeur vide requis pour JAXB
    }

    public ErrorResponse(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
