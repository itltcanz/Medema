package dev.itltcanz.medema.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Scan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "INTEGER")
  Long id;

  @ManyToOne
  Detector detector;

  byte metal;

  LocalDateTime time;

  public Scan(Detector detector, byte metal, LocalDateTime time) {
    this.detector = detector;
    this.metal = metal;
    this.time = time;
  }

  // Используется для ui
  @SuppressWarnings("unused")
  public String getDetectorId() {
    return detector.getId();
  }

  // Используется для ui
  @SuppressWarnings("unused")
  public String getLocationName() {
    return detector.getLocationName();
  }
}
