package com.todolist.todoapi.dto;

import lombok.Data; //gera getters e setters automaticamente, assim como construtor

@Data
public class RegisterRequestDTO {

    private String username;
    private String email;
    private String password;
    private String fullName;
}
