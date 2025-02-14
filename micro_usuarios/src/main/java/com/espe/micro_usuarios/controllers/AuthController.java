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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    // Login para usuario
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Usuarios usuario) throws Exception {
        // Autenticación de usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUser(), usuario.getPassword())
        );

        // Obtener el usuario completo desde la base de datos para obtener sus roles
        Usuarios usuarioDB = usuarioService.findByUser(usuario.getUser()).orElse(null);

        if (usuarioDB != null) {
            // Obtener los roles del usuario desde la base de datos
            List<String> roles = Arrays.asList(usuarioDB.getRoles().split(",")); // Separa los roles por coma

            // Generar el token con los roles obtenidos
            final String token = jwtUserDetailsService.generateToken(usuario.getUser(), roles);

            // Obtener los detalles del usuario para enviar en la respuesta
            UsuarioDTO usuarioDTO = usuarioService.obtenerDatosUsuario(usuario.getUser());

            // Construir la respuesta con el token y los datos del usuario
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
        } else {
            return ResponseEntity.badRequest().body("Usuario no encontrado o contraseña incorrecta");
        }
    }

    // Obtener datos de un usuario (solo para usuario normal)
    @GetMapping("/user/{username}")
    public ResponseEntity<UsuarioDTO> getUserData(@PathVariable String username) {
        UsuarioDTO usuarioDTO = usuarioService.obtenerDatosUsuario(username);
        return ResponseEntity.ok(usuarioDTO);
    }

    // Endpoint para crear recurso ADMIN
    @PostMapping("/admin")
    public ResponseEntity<String> createAdminResource(@RequestBody Usuarios usuario) {
        return ResponseEntity.ok("Recurso creado por ADMIN");
    }

    // Obtener datos de un usuario específico por ID (para admin)
    @GetMapping("/admin/{id}")
    public ResponseEntity<UsuarioDTO> getAdminUserData(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = usuarioService.obtenerDatosUsuario(id);
        return usuarioDTO.isPresent() ? ResponseEntity.ok(usuarioDTO.get()) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo usuario (solo admin)
    @PostMapping("/admin/user")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody Usuarios usuario) {
        // Llamamos al servicio para guardar el nuevo usuario
        Usuarios newUsuario = usuarioService.save(usuario);

        // Devolvemos una respuesta con los datos del nuevo usuario
        return ResponseEntity.ok(new UsuarioDTO(
                newUsuario.getId(),
                newUsuario.getNombre(),
                newUsuario.getApellido(),
                newUsuario.getEmail(),
                newUsuario.getTelefono(),
                newUsuario.getFechaNacimiento(),
                newUsuario.getCreadoEn()
        ));
    }

    // Actualizar un usuario (solo admin)
    @PutMapping("/admin/user/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable Long id, @RequestBody Usuarios usuario) {
        Optional<Usuarios> existingUser = usuarioService.findById(id);
        if (existingUser.isPresent()) {
            usuario.setId(id);
            Usuarios updatedUser = usuarioService.save(usuario);
            return ResponseEntity.ok(new UsuarioDTO(updatedUser.getId(), updatedUser.getNombre(), updatedUser.getApellido(), updatedUser.getEmail(), updatedUser.getTelefono(), updatedUser.getFechaNacimiento(), updatedUser.getCreadoEn()));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar un usuario (solo admin)
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<Usuarios> existingUser = usuarioService.findById(id);
        if (existingUser.isPresent()) {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado");
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener todos los usuarios (solo admin)
    @GetMapping("/admin/users")
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        List<Usuarios> usuarios = usuarioService.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(user -> new UsuarioDTO(user.getId(), user.getNombre(), user.getApellido(), user.getEmail(), user.getTelefono(), user.getFechaNacimiento(), user.getCreadoEn()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }
}
