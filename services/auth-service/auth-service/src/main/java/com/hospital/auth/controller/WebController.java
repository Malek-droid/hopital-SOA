package com.hospital.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    
    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "forward:/signup.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/dashboard.html";
    }

    @GetMapping("/rendezvous-dashboard")
    public String rendezvousDashboard() {
        return "forward:/rendezvous-dashboard.html";
    }
}