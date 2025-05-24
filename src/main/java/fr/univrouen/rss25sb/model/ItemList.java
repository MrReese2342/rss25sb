package fr.univrouen.rss25sb.model;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "rss25SB")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemList {

    @XmlElement(name = "item")
    private List<Item> items;

    public ItemList() {}

    public ItemList(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
