package com.chatlocal.chatlocal.controller;


import com.chatlocal.chatlocal.dto.RegisterRequest;
import com.chatlocal.chatlocal.dto.UtilisateurDto;
import com.chatlocal.chatlocal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurDto register(@Valid @RequestBody RegisterRequest request) {
        return authService.inscrire(request);
    }
}