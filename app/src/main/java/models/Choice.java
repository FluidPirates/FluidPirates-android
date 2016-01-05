package models;

/**
 * Created by dorian on 1/5/16.
 */
public class Choice {
    private int id;
    private String name;

    public Choice(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}