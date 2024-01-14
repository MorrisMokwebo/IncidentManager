package com.example.ims.controller;

import com.example.ims.model.Incident;
import com.example.ims.model.User;
import com.example.ims.service.IncidentService;
import com.example.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class IncidentController {

    private final IncidentService incidentService;
    private final UserService userService;

    @Autowired
    public IncidentController(IncidentService incidentService, UserService userService) {
        this.incidentService = incidentService;
        this.userService = userService;
    }


    @GetMapping("/incidents")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareIncidentsListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "pages/incidents";
    }

    @GetMapping("/incidents/in-progress")
    public String listTasksInProgress(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareIncidentsListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "pages/incidents";
    }

    private void prepareIncidentsListModel(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("incidents", incidentService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/incident/create")
    public String showEmptyIncidentForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Incident incident = new Incident();
        incident.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            incident.setOwner(user);
        }
        model.addAttribute("incident", incident);
        return "forms/new-incident";
    }

    @PostMapping("/incident/create")
    public String createTask(Incident incident, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/new-incident";
        }
        incidentService.createIncident(incident);

        return "redirect:/incidents";
    }

    @GetMapping("/incident/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("incident", incidentService.getIncidentById(id));
        return "forms/incident-edit";
    }

    @PostMapping("/incident/edit/{id}")
    public String updateTask(Incident incident, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/incident-edit";
        }
        incidentService.updateIncident(id,incident);
        return "redirect:/incidents";
    }

    @GetMapping("/incident/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return "redirect:/incidents";
    }

    @GetMapping("/incident/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        incidentService.setIncidentCompleted(id);
        return "redirect:/incidents";
    }

    @GetMapping("/incident/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        incidentService.setIncidentNotCompleted(id);
        return "redirect:/incidents";
    }
}
