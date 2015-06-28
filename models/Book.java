package models;

import persisters.ItemType;

public class Book extends Item {

    public Book(Integer id, String name, String link) {
        super(id, name, link, ItemType.BOOK);
    }

}
