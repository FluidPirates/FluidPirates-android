package models;

import java.util.ArrayList;
import java.util.List;

public class Proposition {
    private long id;
    private String name;
    private String description;
    private ArrayList choices;

    public Proposition(long id, String name, String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);

        this.choices = new ArrayList<Choice>();
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

    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

