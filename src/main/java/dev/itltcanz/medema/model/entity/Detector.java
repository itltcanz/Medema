package dev.itltcanz.medema.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Detector {
    @Id
    private String id;
    private String ip;
    private String port;
    @ManyToOne
    private Location location;

    public String getLocationName() {
        return location.getName();
    }

}
