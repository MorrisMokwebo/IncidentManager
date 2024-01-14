package com.example.ims.repository;

import com.example.ims.model.Incident;
import com.example.ims.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident,Long> {

    List<Incident> findByOwnerOrderByDateDesc(User user);

}
