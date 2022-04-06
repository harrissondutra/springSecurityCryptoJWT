package com.example.springsecuritycrypto.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecuritycrypto.data.DetalheUsuarioData;
import com.example.springsecuritycrypto.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public static final int TOKEN_EXPIRED = 600_000;
    public static final String TOKEN_SENHA = "fcc36c89-0bc8-43db-9b72-6b7d0fd885c8";

    public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Usuario usuario = new ObjectMapper()
                    .readValue(request.getInputStream(), Usuario.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(),
                    usuario.getSenha(),
                    new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException("falha ao autenticar o usuário", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        DetalheUsuarioData usuarioData = (DetalheUsuarioData) authResult.getPrincipal();

        String token = JWT.create().withSubject(usuarioData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRED))
                .sign(Algorithm.HMAC512(TOKEN_SENHA));

        response.getWriter().write(token);
        response.getWriter().flush();
    }


}
