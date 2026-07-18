package com.todolist.todoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice               // escuta toda a aplicação esperando exceções serem lançadas
public class GlobalExceptionHandler {

    private Map<String, Object> buildError(HttpStatus status, String message){  //map guarda os dados como chave/valor
        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());    //define as "chaves valor" do map que será usado
        error.put("message", message);
        return error;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex){ //caso o usuário não seja encontrado devolve um erro 404
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) { //captura erros genericos e lança a exceção generica http 400 e lança a mensagem
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }


}
