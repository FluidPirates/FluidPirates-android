package models;

public class Group {
    private long id;
    private String name;
    private String description;
    private String domain;

    public Group(long id, String name, String description, String domain) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setDomain(domain);
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}

