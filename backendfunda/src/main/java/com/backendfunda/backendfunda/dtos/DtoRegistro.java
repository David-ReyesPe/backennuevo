package com.backendfunda.backendfunda.dtos;

import lombok.Data;

@Data
public class DtoRegistro {
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;
}
