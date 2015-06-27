package models;

import persisters.ItemType;

public class Item {

    protected Integer id = null;
    protected String name = null;
    protected String link = null;
    protected ItemType type = null;

    protected Item(Integer id, String name, String link, ItemType type) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type.toString();
    }
}
