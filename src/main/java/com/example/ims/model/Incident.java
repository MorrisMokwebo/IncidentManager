package com.example.ims.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
@Setter
@Entity
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incident_id")
    private Long id;

    private String name;

    @Column(length = 1200, nullable = false)
    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyy")
    private LocalDate date;

    private boolean isCompleted;

    private String creatorName;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;



    public long daysLeftUntilDeadline(LocalDate date) {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    public Incident() {
    }

    public Incident(String name,
            String description,
              LocalDate date,
                boolean isCompleted,
                String creatorName) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.creatorName = creatorName;
    }

    public Incident( String name,
             String description,
                 LocalDate date,
                boolean isCompleted,
                String creatorName,
                User owner) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.creatorName = creatorName;
        this.owner = owner;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return isCompleted == incident.isCompleted &&
                Objects.equals(id, incident.id) &&
                name.equals(incident.name) &&
                description.equals(incident.description) &&
                date.equals(incident.date) &&
                Objects.equals(creatorName, incident.creatorName) &&
                Objects.equals(owner, incident.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, date, isCompleted, creatorName, owner);
    }

}
