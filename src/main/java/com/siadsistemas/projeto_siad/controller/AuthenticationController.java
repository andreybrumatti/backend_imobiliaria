package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.AuthenticationDTO;
import com.siadsistemas.projeto_siad.dto.LoginResponseDTO;
import com.siadsistemas.projeto_siad.dto.RegisterDTO;
import com.siadsistemas.projeto_siad.security.TokenService;
import com.siadsistemas.projeto_siad.model.Users;
import com.siadsistemas.projeto_siad.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager; // Classe própria do Spring Security que gerencia autenticação
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @Operation(summary = "Realiza o login do usuário")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password()); // Cria um token de autenticação
        var auth = this.authenticationManager.authenticate(usernamePassword); // Autentica o usuário

        var token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Realiza o cadastro do usuário")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO dto) {
        try {
            Users novoUser = authenticationService.registrarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
