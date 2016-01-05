package models;

public class Poll {
    private int id;
    private String name;
    private String description;
    private Boolean open;

    public Poll(int id, String name, String description, boolean open) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setOpen(open);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getOpen() {
        return open;
    }
}

