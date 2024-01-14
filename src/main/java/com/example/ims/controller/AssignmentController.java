package com.example.ims.controller;

import com.example.ims.model.Incident;
import com.example.ims.model.User;
import com.example.ims.service.IncidentService;
import com.example.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AssignmentController {
    private  final UserService userService;
    private final IncidentService incidentService;

    @Autowired
    public AssignmentController(UserService userService, IncidentService incidentService) {
        this.userService = userService;
        this.incidentService = incidentService;
    }



    @GetMapping("/assignment")
    public String showAssigmentForm(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeIncidents", incidentService.findFreeIncidents());
        return "forms/assignment";
    }

    @GetMapping("/assignment/{userId}")
    public String showUserAssigmentForm(@PathVariable Long userId, Model model) {
        model.addAttribute("selectedUser", userService.getUserById(userId));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeIncidents", incidentService.findFreeIncidents());
        return "forms/assignment";
    }

    @GetMapping("/assignment/assign/{userId}/{incidentId}")
    public String assignIncidentToUser(@PathVariable Long userId, @PathVariable Long incidentId) {
        Incident selectedIncident = incidentService.getIncidentById(incidentId);
        User selectedUser = userService.getUserById(userId);
        incidentService.assignIncidentToUser(selectedIncident,selectedUser);
        return "redirect:/assignment/" + userId;
    }

    @GetMapping("assignment/unassign/{userId}/{taskId}")
    public String unassignTaskFromUser(@PathVariable Long userId, @PathVariable Long incidentId) {
        Incident selectedIncident = incidentService.getIncidentById(incidentId);
        incidentService.unassignIncident(selectedIncident);
        return "redirect:/assignment/" + userId;
    }
}
