package com.example.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    String showLoginForm(){
        return "/forms/login";
    }
}
