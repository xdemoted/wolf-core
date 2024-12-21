package com.wolfco.types;

public class Project {
    String name;
    String description;
    String[] parent;
    String locations;
    State state;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }
    
    public String getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", locations=" + locations +
                ", state=" + state +
                '}';
    }
}
