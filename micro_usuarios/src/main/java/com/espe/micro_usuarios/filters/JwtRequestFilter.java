package com.espe.micro_usuarios.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = Jwts.parser().setSigningKey("secreto").parseClaimsJws(jwt).getBody().getSubject();
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extraer los roles del token
            Claims claims = Jwts.parser().setSigningKey("secreto").parseClaimsJws(jwt).getBody();
            List<String> roles = (List<String>) claims.get("roles");

            // Crear una lista de autoridades (roles) sin "ROLE_"
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new) // Ya no agregamos "ROLE_"
                    .collect(Collectors.toList());

            // Crear un objeto de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
