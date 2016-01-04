package models;

public class Proposition {
    private long id;
    private String name;
    private String description;
    private Integer maximum_of_votes_per_user;
    private Integer number_of_winning_choices;

    public Proposition(long id, String name, String description, Integer maximum_of_votes_per_user) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setMaximum_of_votes_per_user(maximum_of_votes_per_user);
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

    public void setMaximum_of_votes_per_user(Integer maximum_of_votes_per_user) {
        this.maximum_of_votes_per_user = maximum_of_votes_per_user;
    }
}

