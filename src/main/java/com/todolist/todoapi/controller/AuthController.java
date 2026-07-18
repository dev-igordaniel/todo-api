package com.todolist.todoapi.controller;

import com.todolist.todoapi.dto.AuthResponseDTO;
import com.todolist.todoapi.dto.LoginRequestDTO;
import com.todolist.todoapi.dto.RegisterRequestDTO;
import com.todolist.todoapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController (AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")           //recebe os dados, cria o usuário, devolve o token
    public ResponseEntity<AuthResponseDTO> register (@RequestBody RegisterRequestDTO dto){
        AuthResponseDTO response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")               //recebe username + senha, valida, devolve o token
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto){
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
