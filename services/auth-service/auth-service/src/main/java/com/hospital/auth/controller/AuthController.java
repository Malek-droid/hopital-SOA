package com.hospital.auth.controller;

import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.dto.LoginRequest;
import com.hospital.auth.entity.User;
import com.hospital.auth.security.JwtUtil;
import com.hospital.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // ================================
    //        REGISTER
    // ================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            // Vérifier si l'email existe déjà
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, "Erreur: Cet email est déjà utilisé!", null, null));
            }

            // Vérifier si le username existe déjà
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, "Erreur: Ce nom d'utilisateur est déjà utilisé!", null, null));
            }

            // Créer l'utilisateur directement avec l'entité User
            User savedUser = userService.createUser(user);

            // Générer un token JWT
            String token = jwtUtil.generateToken(savedUser.getEmail());

            return ResponseEntity.ok()
                .body(new AuthResponse(token, "Utilisateur créé avec succès!", savedUser.getEmail(), savedUser.getRole()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, "Erreur lors de la création de l'utilisateur: " + e.getMessage(), null, null));
        }
    }

    // ================================
    //           LOGIN
    // ================================
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
        System.out.println("Tentative de login pour: " + loginRequest.getEmail());
        
        // Authentifier l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        System.out.println("Authentification réussie");
        
        // Récupérer l'utilisateur
        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Générer token
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok()
                .body(new AuthResponse(token, "Connexion réussie!", user.getEmail(), user.getRole()));
        } else {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, "Erreur: Utilisateur non trouvé!", null, null));
        }

    } catch (Exception e) {
        System.err.println("ERREUR lors du login: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest()
            .body(new AuthResponse(null, "Erreur: Email ou mot de passe incorrect!", null, null));
    }
}

    // ================================
    //        TEST ENDPOINT
    // ================================
    @GetMapping("/test")
    public String test() {
        return "Auth Service is working!";
    }
}