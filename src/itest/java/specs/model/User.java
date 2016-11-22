package specs.model;

public class User {
    private String name;
    private String id;

    public User(String username, String id) {
        this.name = username;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
