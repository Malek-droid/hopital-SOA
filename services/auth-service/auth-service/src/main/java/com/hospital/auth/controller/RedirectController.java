package com.hospital.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
public class RedirectController {

    // Fallback for clients using the old relative URL /services/RendezvousService on port 8083
    @PostMapping("/services/RendezvousService")
    public ResponseEntity<Void> proxyRendezvous(@RequestBody(required = false) String body) {
        // Redirect to the API Gateway
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create("http://localhost:8082/services/RendezvousService"))
                .build();
    }
}
