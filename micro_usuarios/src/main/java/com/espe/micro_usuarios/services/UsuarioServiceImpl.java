package com.espe.micro_usuarios.services;

import com.espe.micro_usuarios.models.dto.UsuarioDTO;
import com.espe.micro_usuarios.models.entities.Usuarios;
import com.espe.micro_usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

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
    public UsuarioDTO login(String user, String password) {
        Usuarios usuario = repository.findByUserAndPassword(user, password);
        if (usuario != null) {
            return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getTelefono(), usuario.getFechaNacimiento(), usuario.getCreadoEn());
        }
        return null;
    }

    @Override
    public UsuarioDTO obtenerDatosUsuario(String username) {
        Optional<Usuarios> usuario = repository.findByUser(username);
        if (usuario.isPresent()) {
            return new UsuarioDTO(usuario.get().getId(), usuario.get().getNombre(), usuario.get().getApellido(), usuario.get().getEmail(), usuario.get().getTelefono(), usuario.get().getFechaNacimiento(), usuario.get().getCreadoEn());
        }
        return null;
    }
}