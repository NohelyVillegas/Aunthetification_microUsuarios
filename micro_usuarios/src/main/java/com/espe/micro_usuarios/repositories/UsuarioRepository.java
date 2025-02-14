package com.espe.micro_usuarios.repositories;

import com.espe.micro_usuarios.models.entities.Usuarios;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuarios, Long> {

    Usuarios findByUserAndPassword(String user, String password);

    Optional<Usuarios> findByUser(String user);
}