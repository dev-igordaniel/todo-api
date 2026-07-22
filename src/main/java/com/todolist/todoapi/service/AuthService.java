package com.todolist.todoapi.service;

import com.todolist.todoapi.config.JwtTokenProvider;
import com.todolist.todoapi.dto.AuthResponseDTO;
import com.todolist.todoapi.dto.LoginRequestDTO;
import com.todolist.todoapi.dto.RegisterRequestDTO;
import com.todolist.todoapi.entity.User;
import com.todolist.todoapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    //monta e devolve o AuthResponseDTO com os dados do usuário e o token
    private AuthResponseDTO buildResponse(User user){
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthResponseDTO(
                user.getUsername(),
                user.getFullName(),
                user.getCreatedAt().toString(),
                token
        );
    }

    public AuthResponseDTO register (RegisterRequestDTO dto) {
        //verifica se username ou email já estão em uso
        if (userRepository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("Username já está em uso");
        }
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já está em uso");
        }

        //monta o usuário com a senha criptografada
        User user = new User ();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setCreatedAt(LocalDate.now());

        userRepository.save(user);

        return buildResponse(user);
    }

    public AuthResponseDTO login(LoginRequestDTO dto){
        //valida o username e a senha - lança exceção automaticamente se inválido
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        //se chegou aqui, as credenciais são validas - busca o usuário no banco
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return buildResponse(user);
    }
}
