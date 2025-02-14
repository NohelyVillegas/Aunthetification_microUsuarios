package com.espe.micro_usuarios.controllers;

import com.espe.micro_usuarios.models.dto.UsuarioDTO;
import com.espe.micro_usuarios.models.dto.UsuarioLoginResponseDTO;
import com.espe.micro_usuarios.models.entities.Usuarios;
import com.espe.micro_usuarios.services.JwtUserDetailsService;
import com.espe.micro_usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Usuarios usuario) throws Exception {
        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUser(), usuario.getPassword())
        );

        // Generar el token JWT
        final String token = jwtUserDetailsService.generateToken(usuario.getUser());

        // Obtener los datos del usuario
        UsuarioDTO usuarioDTO = usuarioService.obtenerDatosUsuario(usuario.getUser());

        // Crear la respuesta con el token y los datos del usuario
        UsuarioLoginResponseDTO response = new UsuarioLoginResponseDTO(
                usuarioDTO.getId(),
                usuarioDTO.getApellido(),
                usuarioDTO.getNombre(),
                usuarioDTO.getEmail(),
                usuarioDTO.getTelefono(),
                usuarioDTO.getFechaNacimiento(),
                usuarioDTO.getCreadoEn(),
                token
        );

        return ResponseEntity.ok(response);
    }
}