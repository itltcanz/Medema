package dev.itltcanz.medema.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
@Entity
public class Scan {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    @ManyToOne
    Module module;
    byte metal;
    LocalDateTime time;
    public Scan(Module module, byte metal, LocalDateTime time) {
        this.module = module;
        this.metal = metal;
        this.time = time;
    }

    public Scan() {

    }

    public Module getModule() {
        return module;
    }

    public String getModuleId() {
        return module.getId();
    }

    public String getLocation() {
        return module.getLocation();
    }

    public byte getMetal() {
        return metal;
    }

    public String getTimeString() {
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            return time.format(formatter);
        }
        return "н.д.";
    }
    public void setModule(Module module) {
        this.module = module;
    }
}
