package com.espe.micro_usuarios.services;

import com.espe.micro_usuarios.models.dto.UsuarioDTO;
import com.espe.micro_usuarios.models.dto.UsuarioLoginResponseDTO;
import com.espe.micro_usuarios.models.entities.Usuarios;
import com.espe.micro_usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public List<Usuarios> findAll() {
        return (List<Usuarios>) repository.findAll();
    }

    @Override
    public Optional<Usuarios> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Usuarios save(Usuarios usuarios) {
        return repository.save(usuarios);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UsuarioLoginResponseDTO login(String user, String password) {
        Usuarios usuario = repository.findByUserAndPassword(user, password);
        if (usuario != null) {
            List<String> roles = Arrays.asList(usuario.getRoles().split(","));
            String token = jwtUserDetailsService.generateToken(user, roles);
            return new UsuarioLoginResponseDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getFechaNacimiento(),
                    usuario.getCreadoEn(),
                    token
            );
        }
        return null;
    }

    @Override
    public UsuarioDTO obtenerDatosUsuario(String username) {
        Usuarios usuario = repository.findByUser(username).orElse(null);
        if (usuario != null) {
            return new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getFechaNacimiento(),
                    usuario.getCreadoEn()
            );
        }
        return null;
    }

    @Override
    public Optional<UsuarioDTO> obtenerDatosUsuario(Long id) {
        return repository.findById(id).map(usuario -> new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento(),
                usuario.getCreadoEn()
        ));
    }

    @Override
    public Optional<Usuarios> findByUser(String user) {
        return repository.findByUser(user);
    }
}