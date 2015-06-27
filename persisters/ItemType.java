package persisters;

public enum ItemType {

    BOOK("book");

    private String type = null;

    ItemType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
