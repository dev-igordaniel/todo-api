package com.todolist.todoapi.service;

import com.todolist.todoapi.entity.User;
import com.todolist.todoapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService {    //"ensina" ao Spring Security que para encontrar um usuário deve usar esta classe

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user = userRepository.findByUsername(username)
                // Caso não encontre o usuário, lança uma UsernameNotFoundException com a mensagem informada.
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));


        // Se nenhuma exceção for lançada, converte a entidade User em um UserDetails,
        // que é o formato esperado pelo Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
