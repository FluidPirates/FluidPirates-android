package models;

public class Scrutin {
    private long id;
    private String name;
    private String description;
    private Boolean open;
    private Boolean closed;



    public Scrutin(long id, String name, String description, boolean closed, boolean open) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setClosed(closed);
        this.setOpen(open);
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
    

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }
    public Boolean getOpen() {
        return open;
    }
}

