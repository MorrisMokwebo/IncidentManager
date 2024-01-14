package com.example.ims.dataloader;

import com.example.ims.model.Incident;
import com.example.ims.model.Role;
import com.example.ims.model.User;
import com.example.ims.service.IncidentService;
import com.example.ims.service.RoleService;
import com.example.ims.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private UserService userService;
    private IncidentService incidentService;
    private RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Value("${default.admin.mail}")
    private String defaultAdminMail;
    @Value("${default.admin.name}")
    private String defaultAdminName;
    @Value("${default.admin.password}")
    private String defaultAdminPassword;
    @Value("${default.admin.image}")
    private String defaultAdminImage;

    @Autowired
    public InitialDataLoader(UserService userService, IncidentService incidentService, RoleService roleService) {
        this.userService = userService;
        this.incidentService = incidentService;
        this.roleService = roleService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //ROLES --------------------------------------------------------------------------------------------------------
        roleService.createRole(new Role("ADMIN"));
        roleService.createRole(new Role("USER"));
        roleService.findAll().stream().map(role -> "saved role: " + role.getRole()).forEach(logger::info);

        //USERS --------------------------------------------------------------------------------------------------------
        //1
        User admin = new User(
                defaultAdminMail,
                defaultAdminName,
                defaultAdminPassword,
                defaultAdminImage);
        userService.createUser(admin);
        userService.changeRoleToAdmin(admin);

        //2
        User manager = new User(
                "devmanager@mail.com",
                "Development Manager",
                "112233",
                "images/admin.png");
        userService.createUser(manager);
        userService.changeRoleToAdmin(manager);

        //3
        userService.createUser(new User(
                "Dev1@mail.com",
                "Dev1",
                "112233",
                "images/mark.jpg"));

        //4
        userService.createUser(new User(
                "Dev2@mail.com",
                "Dev2",
                "112233",
                "images/ann.jpg"));

        //5
        userService.createUser(new User(
                "Support1@mail.com",
                "Support1",
                "112233",
                "images/ralf.jpg"));

        //6
        userService.createUser(new User(
                "Support2@mail.com",
                "Support2",
                "112233",
                "images/kate.jpg"));

        //7
        userService.createUser(new User(
                "Dev3@mail.com",
                "Dev3",
                "112233",
                "images/tom.jpg"));

        userService.findAll().stream()
                .map(u -> "saved user: " + u.getName())
                .forEach(logger::info);



        LocalDate today = LocalDate.now();


        //1
        incidentService.createIncident(new Incident(
                "Customer got an service unavailable when trying to transfer of funds  ",
                "Customer got an service unavailable multiples times when trying to  transfer of funds",
                today.minusDays(40),
                true,
                userService.getUserByEmail("Support2@mail.com").getName(),
                userService.getUserByEmail("Support2@mail.com")
        ));

        //2
        incidentService.createIncident(new Incident((
                "Customer allowed to open account under the age of 16",
                "Customer under the age of 16 was able to open account",
                today.minusDays(30),
                true,
                userService.getUserByEmail("Support1@mail.com").getName(),
                userService.getUserByEmail("Support1@mail.com")
        ));

        //3
        incidentService.createIncident(new Incident(
                "Customer Debited twice",
                "Customer debited twice when he was supposed to be debited once",
                today.minusDays(20),
                true,
                userService.getUserByEmail("Dev1@mail.com").getName(),
                userService.getUserByEmail("Dev1@mail.com")
        ));



        //17
        incidentService.createIncident(new Incident(
                "account creation service is  unavailable",
                "The server hosting the account creation service is  unavailable.",
                today.plusDays(12),
                false,
                userService.getUserByEmail("devmanager@mail.com").getName()
        ));

        //18
        incidentService.createIncident(new Incident(
                "Application allowed incorrect and incomplete data",
                " Incorrect or incomplete data input on the account creation form.",
                today.plusDays(14),
                false,
                userService.getUserByEmail("devmanager@mail.com").getName()
        ));

        //19
        incidentService.createIncident(new Incident(
                "session timed out very fast",
                "Users take too long to complete the account creation process, and their session times out.",
                today.plusDays(16),
                false,
                userService.getUserByEmail("devmanager@mail.com").getName()
        ));

        //20
        incidentService.createIncident(new Incident(
                "Delays in receiving the verification email",
                " Delays in receiving the verification email required to confirm the account.",
                today.plusDays(18),
                false,
                userService.getUserByEmail("devmanager@mail.com").getName()
        ));

        incidentService.findAll().stream().map(t -> "saved task: '" + t.getName()
                + "' for owner: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
    }

    private String getOwnerNameOrNoOwner(Incident incident) {
        return incident.getOwner() == null ? "no owner" : incident.getOwner().getName();
    }
}
