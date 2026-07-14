package com.todolist.todoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration              //Esta anotação diz que o Spring deve carregar essa classe como configuração ao iniciar
@EnableWebSecurity          //ativa o filter chain e o obriga que os filtros sejam carregados antes da requisição chega rno controller
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean                                               //chama o metodo na inicialização pelo spring
    public SecurityFilterChain securityFilterChain(     //nesse metodo o spring passa o http security para ser configurado e guarda seu valor
            HttpSecurity http)                          //HttpSecurity é obj de configuração criado pelo spring security, é onde definimos as regras das requisiçoes
            throws Exception {

                http

                        .csrf(                                  //proteção contra ataque csrf (JWT não utiliza cookie)
                                csrf -> csrf.disable())

                        .sessionManagement(                                                  //configura se o spring vai utilizar sessões
                                session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))     //define a politica de sessão como "sem estado" o servidor não guarda informações sobre o cliente entre as requisições

                        .authorizeHttpRequests(                                              //regras de autorização (quem pode acessar cada rota)
                                auth -> auth
                                        .requestMatchers("/auth/**").permitAll()            //pertime que todos acessem sem atutenticação a rota /auth/**
                                        .anyRequest().authenticated())                               //todas as outras requisições precisam ser autenticadas

                        .addFilterBefore(                               //garante que o token é checado antes do UsernamePasswordAuthenticationFilter
                                jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
            }

    @Bean
    public PasswordEncoder passwordEncoder(){  //esse é um obj q pega uma senha e retorna seu hash
        return new BCryptPasswordEncoder();
    }

    @Bean
    // o AuthenticationManager é usado no login para validar username + senha
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();   //esse authentication manager
    }

}
