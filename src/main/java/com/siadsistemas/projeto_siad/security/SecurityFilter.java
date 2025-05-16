package com.siadsistemas.projeto_siad.security;

import com.siadsistemas.projeto_siad.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter { //Essa classe é responsável por interceptar as requisições e verificar se o token é válido

    private final TokenService tokenService; //Injetando o TokenService para validar o token
    private final UsersRepository usersRepository; //Injetando o UsersRepository para buscar o usuário no banco de dados

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Ignora autenticação para rotas públicas
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if(token != null){
            var login = tokenService.validateToken(token); //Validando o token
            UserDetails user = usersRepository.findByLogin(login);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); //Criando a autenticação do usuário
            SecurityContextHolder.getContext().setAuthentication(authentication);//salvando autenticação no contexto de segurança do Spring
        }

        filterChain.doFilter(request, response); //Se o token for nulo, continua a execução da requisição normalmente
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null) return null;

        return  authorizationHeader.replace("Bearer ", ""); //Retirando o Bearer do token, pegando apenas o token
    }
}
