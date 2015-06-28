package models.models;

public class User implements Comparable<User> {

    protected Integer id = null;

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(User item) {
        return equals(item) ? 0 : 1;
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) {
            return true;
        }

        if (!(aThat instanceof User)) {
            return false;
        }

        User that = (User)aThat;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id;
    }
}
