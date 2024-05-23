package dev.itltcanz.medema.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Module {
    @Id
    private String id;
    private String location;

    public Module(String id, String location) {
        this.id = id;
        this.location = location;
    }

    public Module() {
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }
}
