package com.backendfunda.backendfunda.dtos;

import lombok.Data;

@Data
public class DtoPerfil {
    private String accessToken;
    private String tokenType = "Bearer"; // Elimina el espacio aquí
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String imagen;

    public DtoPerfil(String accessToken, String nombre, String apellido, String telefono, String correo, String imagen) {
        this.accessToken = accessToken;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.imagen = imagen;
    }
}
