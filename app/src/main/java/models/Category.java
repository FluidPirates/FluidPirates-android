package models;

public class Category {
    private long id;
    private String name;
    private String description;

    public Category(long id, String name, String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

