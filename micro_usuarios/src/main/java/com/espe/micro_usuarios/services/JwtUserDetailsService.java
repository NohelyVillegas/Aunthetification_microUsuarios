package com.espe.micro_usuarios.services;

import com.espe.micro_usuarios.models.entities.Usuarios;
import com.espe.micro_usuarios.repositories.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos
        Usuarios usuario = usuarioRepository.findByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Crear un UserDetails con los datos del usuario
        return new User(
                usuario.getUser(),
                usuario.getPassword(),
                new ArrayList<>() // Roles (puedes agregarlos si es necesario)
        );
    }

    // Método para generar un token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Establece el sujeto del token (el nombre de usuario)
                .setIssuedAt(new Date()) // Establece la fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expira en 10 horas
                .signWith(SignatureAlgorithm.HS512, "secreto") // Firma el token con una clave secreta
                .compact(); // Convierte el token en una cadena compacta
    }
}