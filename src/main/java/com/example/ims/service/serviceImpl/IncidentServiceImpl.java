package com.example.ims.service.serviceImpl;

import com.example.ims.model.Incident;
import com.example.ims.model.User;
import com.example.ims.repository.IncidentRepository;
import com.example.ims.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService {

    private IncidentRepository incidentRepository;

    @Autowired
    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public void createIncident(Incident incident) {
        incidentRepository.save(incident);
    }

    @Override
    public void updateIncident(Long id, Incident updateIncident) {
        Incident incident = incidentRepository.getOne(id);
        incident.setName(updateIncident.getName());
        incident.setDescription(updateIncident.getDescription());
        incident.setDate(updateIncident.getDate());
        incidentRepository.save(incident);
    }

    @Override
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    @Override
    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> findByOwnerOrderByDateDesc(User user) {
        return incidentRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override
    public void setIncidentCompleted(Long id) {
        Incident incident = incidentRepository.getReferenceById(id);
        incident.setCompleted(true);
        incidentRepository.save(incident);

    }

    @Override
    public void setIncidentNotCompleted(Long id) {
        Incident incident = incidentRepository.getOne(id);
        incident.setCompleted(false);
        incidentRepository.save(incident);

    }

    @Override
    public List<Incident> findFreeIncidents() {

        return incidentRepository.findAll().stream()
                .filter(incident -> incident.getOwner() == null && !incident.isCompleted())
                .collect(Collectors.toList());
    }

    @Override
    public Incident getIncidentById(Long incidentId) {
        return incidentRepository.findById(incidentId).orElse(null);
    }

    @Override
    public void assignIncidentToUser(Incident incident, User user) {
        incident.setOwner(user);
        incidentRepository.save(incident);
    }

    @Override
    public void unassignIncident(Incident incident) {
        incident.setOwner(null);
        incidentRepository.save(incident);
    }
}
