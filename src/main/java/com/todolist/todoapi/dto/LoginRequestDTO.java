package com.todolist.todoapi.dto;

import lombok.Data; //gera getters e setters automaticamente, assim como construtor

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
}
