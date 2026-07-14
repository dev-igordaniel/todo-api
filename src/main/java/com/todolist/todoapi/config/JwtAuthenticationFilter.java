package com.todolist.todoapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {         //esse extends garante que seja executado uma vez por requisição

    private final JwtTokenProvider jwtTokenProvider;                        //depois que um obj é atribuído ele não poderá ser trocado por outro
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    private String getTokenFromRequest(HttpServletRequest request){                 //recebe a requisição completa em forma de obj
        String bearerToken = request.getHeader("Authorization");                 //pega somente  o token (Authorization) desse obj e salva como bearerToken

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){               //verifica s eo token não é nulo e se começa com Bearer
            return bearerToken.substring(7);                              //remove os 7 caracteres iniciais deixando somente o token (remove o "Bearer ")
        }

        return null;                                                                //sem token
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,             //tudo que o cliente enviou
            HttpServletResponse response,           //a resposta que será enviada ao cliente
            FilterChain filterChain)                //recebe a cadeia de filtros da requisição até chegar ao controller

        throws ServletException, IOException {      //define que dentre deste metodo estas duas exceções pode acontecer e informa o spring que não serão tratadas neste metodo

            String token = getTokenFromRequest(request);

            if (token != null && jwtTokenProvider.validateToken(token)){

                String username = jwtTokenProvider.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(                        //cria um novo obj para representar o cliente autenticado
                                userDetails,
                                null,                                         //não guardamos a senha. O JWT já provou a identidade do usuário
                                userDetails.getAuthorities());                          //informa as permissões de usuário (neste projeto não há distinção)

                authentication.setDetails(                                              //adiciona detalhes da requisição dentro do obj acima criado
                        new WebAuthenticationDetailsSource().buildDetails(request));    //cria o obj e chama o metodo com detalhes tipo IP

                SecurityContextHolder.getContext().setAuthentication(authentication);   //pega o obj de autenticação e registra/guarda no contexto de segurança dessa requisição.
            }                                                                           //esse security context serve para que em outras etapas a aplicação consiga identificar quem está logado

            filterChain.doFilter(request, response);                                    //termina este filtro, continua a cadeia de filtros até o controller
        }
}
