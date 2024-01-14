package com.example.ims.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(length = 10)
    private String password;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'images/user.png'")
    private String photo;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Incident> incidentsOwned;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public User(){};

    public List<Incident> getIncidentsCompleted() {
        return incidentsOwned.stream()
                .filter(Incident::isCompleted)
                .collect(Collectors.toList());
    }

    public List<Incident> getIncidentsInProgress() {
        return incidentsOwned.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }

    public boolean isAdmin() {
        String roleName = "ADMIN";
        return roles.stream().map(Role::getRole).anyMatch(roleName::equals);
    }


    public User(String email,
                String name,
                 String password,
                String photo) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.photo = photo;
    }

    public User(String email,
                String name,
                String password,
                String photo,
                List<Incident> tasksOwned,
                List<Role> roles) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.photo = photo;
        this.incidentsOwned = tasksOwned;
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                password.equals(user.password) &&
                Objects.equals(photo, user.photo) &&
                Objects.equals(incidentsOwned, user.incidentsOwned) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, photo, incidentsOwned, roles);
    }
}



