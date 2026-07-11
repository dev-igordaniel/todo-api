package com.todolist.todoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data                   //gera getters e setters automaticamente
@AllArgsConstructor     //gera um construtor com todos os atributos da classe.
public class AuthResponseDTO {

    private String username;
    private String fullName;
    private String createdAt;
    private String token;
}