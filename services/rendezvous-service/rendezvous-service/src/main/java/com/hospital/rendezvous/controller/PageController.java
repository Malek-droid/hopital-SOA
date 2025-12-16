package com.hospital.rendezvous.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/rendezvous")
    public String rendezvousPage() {
        return "forward:/rendezvous.html";
    }
    
    @GetMapping("/")
    public String index() {
        return "forward:/rendezvous.html";
    }
}