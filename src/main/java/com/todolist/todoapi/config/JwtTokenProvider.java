package com.todolist.todoapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component                                                  //cria um objeto dessa classe e gerencia ela
public class JwtTokenProvider {                             //gera, valida e lê informações do token

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;                             //Long guarda mais caracteres que o Integer. se o token fosse de dias o integer estouraria o valor

    private SecretKey getSigninKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());    //getBytes converte a chave secreta em bytes
    }                                                       //o Keys.hmacShaKeyFor converte estes bytes em obj SecretKey

    public String generateToken(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()               //cria o token Jwt
                .subject(username)          //define o dono
                .issuedAt(now)              //quando foi criado
                .expiration(expiryDate)     //quando expira
                .signWith(getSigninKey())   //gera assinatura
                .compact();                 //transforma em uma String/token
    }

    public String getUsernameFromToken(String token){
                                                                //claims guarda os dados do payload d
        Claims claims = Jwts.parser()                           //Jwts.parser é um metodo para ler e validar o token
                .verifyWith(getSigninKey())                     //informa a chave que vai ser usada para verificar a assinatura
                .build()                                        // monta o verificador
                .parseSignedClaims(token)                       //verifica se a assinatura do token é valido
                .getPayload();                                  //devolve o conteúdo do token depois de validado

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
