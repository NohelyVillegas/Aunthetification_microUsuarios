package com.espe.micro_usuarios.models.dto;

import java.util.Date;

public class UsuarioLoginResponseDTO extends UsuarioDTO {
    private String token;

    public UsuarioLoginResponseDTO(Long id, String nombre, String apellido, String email, String telefono, Date fechaNacimiento, Date creadoEn, String token) {
        super(id, nombre, apellido, email, telefono, fechaNacimiento, creadoEn);
        this.token = token;
    }

    public UsuarioLoginResponseDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}