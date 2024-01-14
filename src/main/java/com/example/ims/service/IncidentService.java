package com.example.ims.service;

import com.example.ims.model.Incident;
import com.example.ims.model.User;

import java.util.List;

public interface IncidentService {


    void createIncident(Incident incident);

    void updateIncident(Long id, Incident incident);

    void deleteIncident(Long id);

    List<Incident> findAll();

    List<Incident> findByOwnerOrderByDateDesc(User user);

    void setIncidentCompleted(Long id);

    void setIncidentNotCompleted(Long id);

    List<Incident> findFreeIncidents();

    Incident getIncidentById(Long incidentId);

    void assignIncidentToUser(Incident incident, User user);

    void unassignIncident(Incident incident);
}
