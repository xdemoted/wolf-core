package com.wolfco.types;

import java.util.List;

public class Project {
    String name;
    String description;
    String parent;
    List<String> objectives;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }
}
