package models;

import persisters.ItemType;

public class Item implements Comparable<Item> {

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

    public ItemType getType() {
        return type;
    }

    @Override
    public int compareTo(Item item) {
        return equals(item) ? 0 : 1;
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) {
            return true;
        }

        if (!(aThat instanceof Item)) {
            return false;
        }

        Item that = (Item)aThat;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id;
    }
}
