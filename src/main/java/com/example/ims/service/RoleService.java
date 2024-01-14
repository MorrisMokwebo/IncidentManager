package com.example.ims.service;

import com.example.ims.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    List<Role> findAll();

}
