package com.example.ims.service.serviceImpl;

import com.example.ims.model.Role;
import com.example.ims.model.User;
import com.example.ims.repository.IncidentRepository;
import com.example.ims.repository.RoleRepository;
import com.example.ims.repository.UserRepository;
import com.example.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {


    private static final String ADMIN="ADMIN";
    private static final String USER="USER";

    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, IncidentRepository incidentRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.incidentRepository = incidentRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRole(USER);
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        return userRepository.save(user);
    }

    @Override
    public User changeRoleToAdmin(User user) {
        Role adminRole = roleRepository.findByRole(ADMIN);
        user.setRoles(new ArrayList<>(Collections.singletonList(adminRole)));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isUserEmailPresent(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getOne(id);
        user.getIncidentsOwned().forEach(incident -> incident.setOwner(null));
        userRepository.delete(user);

    }
}
