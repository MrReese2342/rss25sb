package fr.univrouen.rss25sb.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.Date;

@Entity
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient // Pour que JAXB n’utilise pas cette valeur générée automatiquement
    private Long dbId;

    @XmlAttribute(name = "id", required = true)
    @Column(nullable = false)
    private int id;

    @XmlElement
    private String title;

    @XmlElement
    private String description;

    @XmlElement
    private String link;

    @XmlElement
    @Temporal(TemporalType.TIMESTAMP)
    private Date pubDate;

    @XmlElement
    private String category;

    // Constructeurs
    public Item() {}

    public Item(int id, String title, String description, String link, Date pubDate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.category = category;
    }

    // Getters/Setters
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
